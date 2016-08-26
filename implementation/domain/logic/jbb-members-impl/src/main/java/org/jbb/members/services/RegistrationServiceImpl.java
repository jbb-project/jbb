/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.services;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

import org.apache.commons.lang3.Validate;
import org.jbb.members.api.exceptions.RegistrationException;
import org.jbb.members.api.model.RegistrationRequest;
import org.jbb.members.api.services.RegistrationService;
import org.jbb.members.dao.MemberRepository;
import org.jbb.members.entities.MemberEntity;
import org.jbb.members.entities.RegistrationMetaDataEntity;
import org.jbb.members.events.MemberRegistrationEvent;
import org.jbb.members.properties.MembersProperties;
import org.jbb.security.api.exceptions.PasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;


@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final MemberRepository memberRepository;

    private final Validator validator;

    private final EventBus eventBus;

    private final MembersProperties properties;

    private final PasswordSaver passwordSaver;

    @Autowired
    public RegistrationServiceImpl(MemberRepository memberRepository, Validator validator,
                                   EventBus eventBus, MembersProperties properties,
                                   PasswordSaver passwordSaver) {
        this.memberRepository = memberRepository;
        this.validator = validator;
        this.eventBus = eventBus;
        this.properties = properties;
        this.passwordSaver = passwordSaver;
    }

    private static void produceException(Set<ConstraintViolation<MemberEntity>> validationResult) {
        throw new RegistrationException(validationResult);
    }

    @Override
    @Transactional
    public void register(RegistrationRequest regRequest) {
        Validate.notNull(regRequest);

        RegistrationMetaDataEntity metaData = RegistrationMetaDataEntity.builder()
                .ipAddress(regRequest.getIPAddress())
                .joinDateTime(LocalDateTime.now())
                .build();

        MemberEntity newMember = MemberEntity.builder()
                .login(regRequest.getLogin())
                .displayedName(regRequest.getDisplayedName())
                .email(regRequest.getEmail())
                .registrationMetaData(metaData)
                .build();

        Set<ConstraintViolation<MemberEntity>> validationResult = validator.validate(newMember);
        if (!validationResult.isEmpty()) {
            produceException(validationResult);
        }

        MemberEntity memberEntity = memberRepository.save(newMember);
        try {
            passwordSaver.save(regRequest);
        } catch (PasswordException e) {
            throw new RegistrationException(Sets.newHashSet());
        }

        publishEvent(memberEntity);
    }

    @Override
    public void allowEmailDuplication(boolean allow) {
        properties.setProperty(MembersProperties.EMAIL_DUPLICATION_KEY, Boolean.toString(allow));
    }

    private void publishEvent(MemberEntity memberEntity) {
        eventBus.post(new MemberRegistrationEvent(memberEntity.getId()));
    }

}
