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

import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.members.CoreConfigMocks;
import org.jbb.members.MembersConfig;
import org.jbb.members.api.exceptions.RegistrationException;
import org.jbb.members.api.services.RegistrationService;
import org.jbb.members.dao.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfigMocks.class, MembersConfig.class, PropertiesConfig.class,
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
        RegistrationRequestImpl registrationRequest = registrationRequest("mark", "Mark", "mark@mark.pl");

        // when
        registrationService.register(registrationRequest);

        // then
        assertThat(repository.countByLogin(registrationRequest.getLogin())).isEqualTo(1);
    }

    @Test(expected = RegistrationException.class)
    public void shouldThrowRegistrationException_whenTriedToRegisterLoginAgain() throws Exception {
        // given
        RegistrationRequestImpl registrationRequest = registrationRequest("john", "John", "john@josh.com");
        RegistrationRequestImpl repeatedLoginRequest = registrationRequest("john", "Johnny", "johnny@josh.com");

        // when
        registrationService.register(registrationRequest);
        registrationService.register(repeatedLoginRequest);
    }

    @Test(expected = RegistrationException.class)
    public void shouldThrowRegistrationException_whenTriedToRegisterDisplayedNameAgain() throws Exception {
        // given
        RegistrationRequestImpl registrationRequest = registrationRequest("john", "John", "john@josh.com");
        RegistrationRequestImpl repeatedNameRequest = registrationRequest("johnny", "John", "johnny@josh.com");

        // when
        registrationService.register(registrationRequest);
        registrationService.register(repeatedNameRequest);
    }

    @Test(expected = RegistrationException.class)
    public void shouldThrowRegistrationException_whenTriedToRegisterEmailAgain_whenDuplicationIsForbidden() throws Exception {
        // given
        RegistrationRequestImpl registrationRequest = registrationRequest("john", "John", "john@john.com");
        RegistrationRequestImpl repeatedNameRequest = registrationRequest("johnny", "Johnny", "john@john.com");

        // when
        registrationService.allowEmailDuplication(false);
        registrationService.register(registrationRequest);
        registrationService.register(repeatedNameRequest);
    }

    @Test
    public void shouldRegister_whenTriedToRegisterEmailAgain_whenDuplicationIsAllowed() throws Exception {
        // given
        RegistrationRequestImpl registrationRequest = registrationRequest("john", "John", "john@john.com");
        RegistrationRequestImpl repeatedNameRequest = registrationRequest("johnny", "Johnnny", "john@john.com");

        // when
        registrationService.allowEmailDuplication(true);
        registrationService.register(registrationRequest);
        registrationService.register(repeatedNameRequest);

        // then
        assertThat(repository.countByEmail(repeatedNameRequest.getEmail())).isEqualTo(2);
    }

    private RegistrationRequestImpl registrationRequest(String login, String displayedName, String email) {
        RegistrationRequestImpl request = new RegistrationRequestImpl();
        request.setLogin(login);
        request.setDisplayedName(displayedName);
        request.setEmail(email);
        request.setIpAddress("127.0.0.1");
        return request;
    }
}