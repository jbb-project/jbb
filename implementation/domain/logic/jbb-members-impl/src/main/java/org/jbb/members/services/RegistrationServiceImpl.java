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
import org.jbb.members.api.exceptions.LoginBusyException;
import org.jbb.members.api.exceptions.RegistrationException;
import org.jbb.members.api.model.Login;
import org.jbb.members.api.model.RegistrationDetails;
import org.jbb.members.api.services.RegistrationService;
import org.jbb.members.dao.MemberRepository;
import org.jbb.members.entities.MemberEntity;
import org.jbb.members.events.MemberRegistrationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private MemberRepository memberRepository;
    private EventBus eventBus;
    @Autowired
    public RegistrationServiceImpl(MemberRepository memberRepository, EventBus eventBus) {
        this.memberRepository = memberRepository;
        this.eventBus = eventBus;
    }

    @Override
    @Transactional(transactionManager = MembersConfig.TRANSACTION_MGR_NAME)
    public void register(RegistrationDetails details) throws RegistrationException {
        Validate.notNull(details);
        checkIsLoginFree(details.getLogin());
        MemberEntity newMember = MemberEntity.builder()
                .login(details.getLogin())
                .displayedName(details.getDisplayedName())
                .email(details.getEmail())
                .registrationDate(LocalDateTime.now())
                .build();
        MemberEntity memberEntity = memberRepository.save(newMember);
        eventBus.post(new MemberRegistrationEvent(memberEntity.getId()));
    }

    private void checkIsLoginFree(Login login) {
        if (memberRepository.countByLogin(login) > 0) {
            throw new LoginBusyException(login);
        }
    }
}
