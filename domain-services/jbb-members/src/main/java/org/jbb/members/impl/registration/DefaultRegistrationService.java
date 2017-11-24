/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.registration;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import java.util.Optional;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.jbb.members.api.registration.RegistrationException;
import org.jbb.members.api.registration.RegistrationMetaData;
import org.jbb.members.api.registration.RegistrationRequest;
import org.jbb.members.api.registration.RegistrationService;
import org.jbb.members.event.MemberRegistrationEvent;
import org.jbb.members.impl.base.MembersProperties;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.members.impl.base.model.validation.create.CreateGroup;
import org.jbb.members.impl.registration.model.RegistrationMetaDataEntity;
import org.jbb.security.api.password.PasswordException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultRegistrationService implements RegistrationService {
    private final MemberRepository memberRepository;
    private final RegistrationMetaDataEntityFactory registrationMetaDataFactory;
    private final MemberEntityFactory memberFactory;
    private final Validator validator;
    private final EventBus eventBus;
    private final MembersProperties properties;
    private final PasswordSaver passwordSaver;

    private static void produceException(Set<ConstraintViolation<?>> validationResult) {
        throw new RegistrationException(validationResult);
    }

    @Override
    @Transactional
    public void register(RegistrationRequest request) {
        Validate.notNull(request);

        RegistrationMetaDataEntity metaData = registrationMetaDataFactory.create(request);
        MemberEntity newMember = memberFactory.create(request, metaData);

        Set<ConstraintViolation<?>> validationResult = Sets.newHashSet();
        validationResult.addAll(validator.validate(newMember, Default.class, CreateGroup.class));

        newMember = memberRepository.save(newMember);
        try {
            passwordSaver.save(request, newMember.getId());
        } catch (PasswordException e) {
            log.debug("Problem with password value during registration of member with username '{}'", request.getUsername(), e);
            validationResult.addAll(e.getConstraintViolations());
        }

        if (!validationResult.isEmpty()) {
            produceException(validationResult);
        }

        publishEvent(newMember);
    }

    @Override
    public boolean isEmailDuplicationAllowed() {
        return properties.allowEmailDuplication();
    }

    @Override
    public void allowEmailDuplication(boolean allow) {
        properties.setProperty(MembersProperties.EMAIL_DUPLICATION_KEY, Boolean.toString(allow));
    }

    @Override
    public RegistrationMetaData getRegistrationMetaData(Long memberId) {
        Validate.notNull(memberId);
        Optional<MemberEntity> member = Optional.ofNullable(memberRepository.findOne(memberId));
        if (member.isPresent()) {
            return member.get().getRegistrationMetaData();
        } else {
            throw new UsernameNotFoundException(String.format("User with username '%s' not found'", memberId));
        }
    }

    private void publishEvent(MemberEntity memberEntity) {
        eventBus.post(new MemberRegistrationEvent(memberEntity.getId()));
    }

}
