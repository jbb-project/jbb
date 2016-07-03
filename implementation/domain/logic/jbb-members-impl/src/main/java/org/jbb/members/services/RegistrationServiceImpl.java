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

import org.jbb.members.MembersConfig;
import org.jbb.members.api.exceptions.RegistrationException;
import org.jbb.members.api.model.RegistrationDetails;
import org.jbb.members.api.services.RegistrationService;
import org.jbb.members.dao.MemberRepository;
import org.jbb.members.entities.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    @Transactional(transactionManager = MembersConfig.TRANSACTION_MGR_NAME)
    public void register(RegistrationDetails details) throws RegistrationException {
        MemberEntity newMember = MemberEntity.builder()
                .login(details.getLogin())
                .displayedName(details.getDisplayedName())
                .email(details.getEmail())
                .build();
        memberRepository.save(newMember);
    }
}
