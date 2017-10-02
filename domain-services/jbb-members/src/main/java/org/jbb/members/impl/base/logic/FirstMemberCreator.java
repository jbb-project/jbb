/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.logic;

import com.google.common.eventbus.Subscribe;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.IPAddress;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.registration.RegistrationRequest;
import org.jbb.members.api.registration.RegistrationService;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.security.api.role.RoleService;
import org.jbb.system.event.DatabaseSettingsChangedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FirstMemberCreator {
    private static final String ADMIN_USERNAME_STRING = "administrator";
    public static final Username ADMIN_USERNAME = Username.builder().value(ADMIN_USERNAME_STRING).build();
    private static final String ADMIN_PSWD_STRING = "administrator";

    private final MemberRepository memberRepository;
    private final RoleService roleService;
    private final RegistrationService registrationService;
    private final JbbEventBus eventBus;

    @PostConstruct
    public void registerToEventBus() {
        eventBus.register(this);
    }

    @Subscribe
    @Transactional
    public void createFirstMemberWithAdministratorRoleIfNeeded(DatabaseSettingsChangedEvent e) {
        if (memberRepository.count() == 0) {
            createAdministrator();
        }
    }


    private void createAdministrator() {
        registrationService.register(new AdminRegistrationRequest());
        Optional<MemberEntity> adminMember = memberRepository.findByUsername(ADMIN_USERNAME);
        adminMember.ifPresent(admin -> roleService.addAdministratorRole(admin.getId()));
    }


    private class AdminRegistrationRequest implements RegistrationRequest {

        @Override
        public Username getUsername() {
            return ADMIN_USERNAME;
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
            return Password.builder().value(ADMIN_PSWD_STRING.toCharArray()).build();
        }

        @Override
        public Password getPasswordAgain() {
            return Password.builder().value(ADMIN_PSWD_STRING.toCharArray()).build();
        }
    }
}
