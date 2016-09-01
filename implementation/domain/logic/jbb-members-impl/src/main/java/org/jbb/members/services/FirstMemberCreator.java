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

import org.jbb.lib.core.vo.Email;
import org.jbb.lib.core.vo.IPAddress;
import org.jbb.lib.core.vo.Login;
import org.jbb.lib.core.vo.Password;
import org.jbb.members.api.model.DisplayedName;
import org.jbb.members.api.model.RegistrationRequest;
import org.jbb.members.api.services.RegistrationService;
import org.jbb.members.dao.MemberRepository;
import org.jbb.security.api.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

@Component
public class FirstMemberCreator {
    private static final Login ADMIN_LOGIN = Login.builder().value("administrator").build();

    private final MemberRepository memberRepository;
    private final RoleService roleService;
    private final RegistrationService registrationService;

    @Autowired
    public FirstMemberCreator(MemberRepository memberRepository, RoleService roleService,
                              RegistrationService registrationService) {
        this.memberRepository = memberRepository;
        this.roleService = roleService;
        this.registrationService = registrationService;
    }

    @PostConstruct
    @Transactional
    public void createFirstMemberWithAdministratorRoleIfNeeded() {
        if (memberRepository.count() == 0) {
            createAdministrator();
        }
    }


    private void createAdministrator() {
        registrationService.register(new AdminRegistratorRequest());
        roleService.addAdministratorRole(ADMIN_LOGIN);
    }


    private class AdminRegistratorRequest implements RegistrationRequest {

        @Override
        public Login getLogin() {
            return ADMIN_LOGIN;
        }

        @Override
        public DisplayedName getDisplayedName() {
            return DisplayedName.builder().value("Administrator").build();
        }

        @Override
        public Email getEmail() {
            return Email.builder().value("example@company.com").build();
        }

        @Override
        public IPAddress getIPAddress() {
            try {
                return IPAddress.builder().value(Inet4Address.getLocalHost().getHostAddress()).build();
            } catch (UnknownHostException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public Password getPassword() {
            return Password.builder().value("administrator".toCharArray()).build();
        }

        @Override
        public Password getPasswordAgain() {
            return Password.builder().value("administrator".toCharArray()).build();
        }
    }
}
