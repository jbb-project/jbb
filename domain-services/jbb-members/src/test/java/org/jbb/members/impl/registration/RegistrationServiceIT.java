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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.IPAddress;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.registration.RegistrationException;
import org.jbb.members.api.registration.RegistrationMetaData;
import org.jbb.members.api.registration.RegistrationRequest;
import org.jbb.members.api.registration.RegistrationService;
import org.jbb.members.impl.BaseIT;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class RegistrationServiceIT extends BaseIT {
    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private MemberRepository repository;

    @Test
    public void shouldRegister_whenRegistrationRequestCorrect() throws Exception {
        // given
        RegistrationRequest registrationRequest = registrationRequest("mark", "Mark",
            "mark@mark.pl", "securedP@ssw0rd", "securedP@ssw0rd");

        // when
        registrationService.register(registrationRequest);

        // then
        assertThat(repository.countByUsername(registrationRequest.getUsername())).isEqualTo(1);
    }

    @Test(expected = RegistrationException.class)
    public void shouldThrowRegistrationException_whenTriedToRegisterUsernameAgain() throws Exception {
        // given
        RegistrationRequest registrationRequest = registrationRequest("john", "John",
            "john@josh.com", "securedP@ssw0rd", "securedP@ssw0rd");
        RegistrationRequest repeatedUsernameRequest = registrationRequest("john", "Johnny",
            "johnny@josh.com", "securedP@ssw0rd", "securedP@ssw0rd");

        // when
        registrationService.register(registrationRequest);
        registrationService.register(repeatedUsernameRequest);
    }

    @Test(expected = RegistrationException.class)
    public void shouldThrowRegistrationException_whenTriedToRegisterDisplayedNameAgain() throws Exception {
        // given
        RegistrationRequest registrationRequest = registrationRequest("john", "John",
            "john@josh.com", "securedP@ssw0rd", "securedP@ssw0rd");
        RegistrationRequest repeatedNameRequest = registrationRequest("johnny", "John",
            "johnny@josh.com", "securedP@ssw0rd", "securedP@ssw0rd");

        // when
        registrationService.register(registrationRequest);
        registrationService.register(repeatedNameRequest);
    }

    @Test(expected = RegistrationException.class)
    public void shouldThrowRegistrationException_whenPasswordsNotMatch() throws Exception {
        // given
        RegistrationRequest registrationRequest = registrationRequest("john", "John",
            "john@josh.com", "securedP@ssw0rd", "anotherPassword");

        // when
        registrationService.register(registrationRequest);
    }

    @Test(expected = RegistrationException.class)
    public void shouldThrowRegistrationException_whenTriedToRegisterEmailAgain_whenDuplicationIsForbidden() throws Exception {
        // given
        RegistrationRequest registrationRequest = registrationRequest("john", "John",
            "john@john.com", "securedP@ssw0rd", "securedP@ssw0rd");
        RegistrationRequest repeatedNameRequest = registrationRequest("johnny", "Johnny",
            "john@john.com", "securedP@ssw0rd", "securedP@ssw0rd");

        // when
        registrationService.allowEmailDuplication(false);
        registrationService.register(registrationRequest);
        registrationService.register(repeatedNameRequest);
    }

    @Test
    public void shouldRegister_whenTriedToRegisterEmailAgain_whenDuplicationIsAllowed() throws Exception {
        // given
        RegistrationRequest registrationRequest = registrationRequest("john", "John",
            "john@john.com", "securedP@ssw0rd", "securedP@ssw0rd");
        RegistrationRequest repeatedNameRequest = registrationRequest("johnny", "Johnnny",
            "john@john.com", "securedP@ssw0rd", "securedP@ssw0rd");

        // when
        registrationService.allowEmailDuplication(true);
        registrationService.register(registrationRequest);
        registrationService.register(repeatedNameRequest);

        // then
        assertThat(repository.countByEmail(repeatedNameRequest.getEmail())).isEqualTo(2);
    }

    @Test
    public void shouldReturnRegistrationMetaData_whenMemberExist() throws Exception {
        // given
        Username username = Username.builder().value("tom").build();
        RegistrationRequest registrationRequest = registrationRequest(username.toString(), "Tom",
            "tom@tom.com", "securedP@ssw0rd", "securedP@ssw0rd");
        registrationService.register(registrationRequest);

        // when
        Optional<MemberEntity> member = repository.findByUsername(username);
        RegistrationMetaData registrationMetaData = registrationService.getRegistrationMetaData(member.get().getId());

        // then
        assertThat(registrationMetaData.getIpAddress()).isNotNull();
        assertThat(registrationMetaData.getJoinDateTime()).isNotNull();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowUserNotFoundException_whenGettingRegistrationMetadataInvoked_andMemberDoNotExist() throws Exception {
        // given
        Long notExistingId = 45543L;

        // when
        registrationService.getRegistrationMetaData(notExistingId);

        // then
        // throw UserNotFoundException
    }

    private RegistrationRequest registrationRequest(String username, String displayedName,
                                                        String email, String password, String passwordAgain) {
        return RegistrationRequest.builder()
            .username(Username.builder().value(username).build())
            .displayedName(DisplayedName.builder().value(displayedName).build())
            .email(Email.builder().value(email).build())
            .password(Password.builder().value(password.toCharArray()).build())
            .passwordAgain(Password.builder().value(passwordAgain.toCharArray()).build())
            .ipAddress(IPAddress.builder().value("127.0.0.1").build())
            .build();
    }
}