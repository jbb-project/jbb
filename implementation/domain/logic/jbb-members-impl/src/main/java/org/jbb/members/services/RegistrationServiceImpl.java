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

import com.google.common.eventbus.EventBus;

import org.apache.commons.lang3.Validate;
import org.jbb.members.MembersConfig;
import org.jbb.members.api.exceptions.RegistrationException;
import org.jbb.members.api.model.RegistrationRequest;
import org.jbb.members.api.services.RegistrationService;
import org.jbb.members.dao.MemberRepository;
import org.jbb.members.entities.MemberEntity;
import org.jbb.members.entities.RegistrationMetaDataEntity;
import org.jbb.members.events.MemberRegistrationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;


@Service
public class RegistrationServiceImpl implements RegistrationService {
    private MemberRepository memberRepository;

    private Validator validator;

    private EventBus eventBus;

    @Autowired
    public RegistrationServiceImpl(MemberRepository memberRepository, Validator validator, EventBus eventBus) {
        this.memberRepository = memberRepository;
        this.validator = validator;
        this.eventBus = eventBus;
    }

    @Override
    @Transactional(transactionManager = MembersConfig.TRANSACTION_MGR_NAME)
    public void register(RegistrationRequest details) throws RegistrationException {
        Validate.notNull(details);

        RegistrationMetaDataEntity metaData = RegistrationMetaDataEntity.builder()
                .ipAddress(details.getIPAddress())
                .joinDateTime(LocalDateTime.now())
                .build();

        MemberEntity newMember = MemberEntity.builder()
                .login(details.getLogin())
                .displayedName(details.getDisplayedName())
                .email(details.getEmail())
                .registrationMetaData(metaData)
                .build();

        Set<ConstraintViolation<MemberEntity>> validationResult = validator.validate(newMember);
        if (!validationResult.isEmpty()) {
            produceException(validationResult);
        }

        MemberEntity memberEntity = memberRepository.save(newMember);
        publishEvent(memberEntity);
    }

    private void produceException(Set<ConstraintViolation<MemberEntity>> validationResult) {
        throw new RegistrationException(validationResult);
    }

    private void publishEvent(MemberEntity memberEntity) {
        eventBus.post(new MemberRegistrationEvent(memberEntity.getId()));
    }

}
