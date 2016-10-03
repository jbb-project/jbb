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

import org.jbb.lib.core.vo.Username;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.members.api.data.RegistrationMetaData;
import org.jbb.members.api.exception.RegistrationException;
import org.jbb.members.api.service.RegistrationService;
import org.jbb.members.impl.MembersConfig;
import org.jbb.members.impl.SecurityConfigMocks;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfigMocks.class, SecurityConfigMocks.class,
        MembersConfig.class, PropertiesConfig.class,
        EventBusConfig.class, DbConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RegistrationServiceIT {
    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private MemberRepository repository;

    @Test
    public void shouldRegister_whenRegistrationRequestCorrect() throws Exception {
        // given
        RegistrationRequestImpl registrationRequest = registrationRequest("mark", "Mark", "mark@mark.pl", "securedP@ssw0rd", "securedP@ssw0rd");

        // when
        registrationService.register(registrationRequest);

        // then
        assertThat(repository.countByUsername(registrationRequest.getUsername())).isEqualTo(1);
    }

    @Test(expected = RegistrationException.class)
    public void shouldThrowRegistrationException_whenTriedToRegisterUsernameAgain() throws Exception {
        // given
        RegistrationRequestImpl registrationRequest = registrationRequest("john", "John", "john@josh.com", "securedP@ssw0rd", "securedP@ssw0rd");
        RegistrationRequestImpl repeatedUsernameRequest = registrationRequest("john", "Johnny", "johnny@josh.com", "securedP@ssw0rd", "securedP@ssw0rd");

        // when
        registrationService.register(registrationRequest);
        registrationService.register(repeatedUsernameRequest);
    }

    @Test(expected = RegistrationException.class)
    public void shouldThrowRegistrationException_whenTriedToRegisterDisplayedNameAgain() throws Exception {
        // given
        RegistrationRequestImpl registrationRequest = registrationRequest("john", "John", "john@josh.com", "securedP@ssw0rd", "securedP@ssw0rd");
        RegistrationRequestImpl repeatedNameRequest = registrationRequest("johnny", "John", "johnny@josh.com", "securedP@ssw0rd", "securedP@ssw0rd");

        // when
        registrationService.register(registrationRequest);
        registrationService.register(repeatedNameRequest);
    }

    @Test(expected = RegistrationException.class)
    public void shouldThrowRegistrationException_whenPasswordsNotMatch() throws Exception {
        // given
        RegistrationRequestImpl registrationRequest = registrationRequest("john", "John", "john@josh.com", "securedP@ssw0rd", "anotherPassword");

        // when
        registrationService.register(registrationRequest);
    }

    @Test(expected = RegistrationException.class)
    public void shouldThrowRegistrationException_whenTriedToRegisterEmailAgain_whenDuplicationIsForbidden() throws Exception {
        // given
        RegistrationRequestImpl registrationRequest = registrationRequest("john", "John", "john@john.com", "securedP@ssw0rd", "securedP@ssw0rd");
        RegistrationRequestImpl repeatedNameRequest = registrationRequest("johnny", "Johnny", "john@john.com", "securedP@ssw0rd", "securedP@ssw0rd");

        // when
        registrationService.allowEmailDuplication(false);
        registrationService.register(registrationRequest);
        registrationService.register(repeatedNameRequest);
    }

    @Test
    public void shouldRegister_whenTriedToRegisterEmailAgain_whenDuplicationIsAllowed() throws Exception {
        // given
        RegistrationRequestImpl registrationRequest = registrationRequest("john", "John", "john@john.com", "securedP@ssw0rd", "securedP@ssw0rd");
        RegistrationRequestImpl repeatedNameRequest = registrationRequest("johnny", "Johnnny", "john@john.com", "securedP@ssw0rd", "securedP@ssw0rd");

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
        RegistrationRequestImpl registrationRequest = registrationRequest(username.toString(), "Tom", "tom@tom.com", "securedP@ssw0rd", "securedP@ssw0rd");
        registrationService.register(registrationRequest);

        // when
        RegistrationMetaData registrationMetaData = registrationService.getRegistrationMetaData(username);

        // then
        assertThat(registrationMetaData.getIpAddress()).isNotNull();
        assertThat(registrationMetaData.getJoinDateTime()).isNotNull();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowUserNotFoundException_whenGettingRegistrationMetadataInvoked_andMemberDoNotExist() throws Exception {
        // given
        Username username = Username.builder().value("not_exist").build();

        // when
        registrationService.getRegistrationMetaData(username);

        // then
        // throw UserNotFoundException
    }

    private RegistrationRequestImpl registrationRequest(String username, String displayedName,
                                                        String email, String password, String passwordAgain) {
        RegistrationRequestImpl request = new RegistrationRequestImpl();
        request.setUsername(username);
        request.setDisplayedName(displayedName);
        request.setEmail(email);
        request.setPassword(password);
        request.setPasswordAgain(passwordAgain);
        request.setIpAddress("127.0.0.1");
        return request;
    }
}