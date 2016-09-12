/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.registration.logic;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

import org.apache.commons.lang3.Validate;
import org.jbb.members.api.data.RegistrationRequest;
import org.jbb.members.api.exception.RegistrationException;
import org.jbb.members.api.service.RegistrationService;
import org.jbb.members.event.MemberRegistrationEvent;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.data.MembersProperties;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.members.impl.registration.model.RegistrationMetaDataEntity;
import org.jbb.security.api.exception.PasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
    private final MemberRepository memberRepository;
    private final RegistrationMetaDataEntityFactory registrationMetaDataFactory;
    private final MemberEntityFactory memberFactory;
    private final Validator validator;
    private final EventBus eventBus;
    private final MembersProperties properties;
    private final PasswordSaver passwordSaver;

    @Autowired
    public RegistrationServiceImpl(MemberRepository memberRepository,
                                   RegistrationMetaDataEntityFactory registrationMetaDataFactory,
                                   MemberEntityFactory memberFactory, Validator validator,
                                   EventBus eventBus, MembersProperties properties,
                                   PasswordSaver passwordSaver) {
        this.memberRepository = memberRepository;
        this.registrationMetaDataFactory = registrationMetaDataFactory;
        this.memberFactory = memberFactory;
        this.validator = validator;
        this.eventBus = eventBus;
        this.properties = properties;
        this.passwordSaver = passwordSaver;
    }

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
        validationResult.addAll(validator.validate(newMember));

        memberRepository.save(newMember);
        try {
            passwordSaver.save(request);
        } catch (PasswordException e) {
            log.warn("Problem with password value during registration of member with username '{}'", request.getUsername(), e);
            validationResult.addAll(e.getConstraintViolations());
        }

        if (!validationResult.isEmpty()) {
            produceException(validationResult);
        }

        publishEvent(newMember);
    }

    @Override
    public void allowEmailDuplication(boolean allow) {
        properties.setProperty(MembersProperties.EMAIL_DUPLICATION_KEY, Boolean.toString(allow));
    }

    private void publishEvent(MemberEntity memberEntity) {
        eventBus.post(new MemberRegistrationEvent(memberEntity.getUsername()));
    }

}
