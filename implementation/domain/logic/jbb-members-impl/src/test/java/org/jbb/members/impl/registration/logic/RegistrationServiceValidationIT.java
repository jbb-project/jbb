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

import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.members.api.exception.RegistrationException;
import org.jbb.members.api.service.RegistrationService;
import org.jbb.members.impl.MembersConfig;
import org.jbb.members.impl.SecurityConfigMocks;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

import javax.validation.ConstraintViolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfigMocks.class, SecurityConfigMocks.class,
        MembersConfig.class, PropertiesConfig.class,
        EventBusConfig.class, DbConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RegistrationServiceValidationIT {
    @Autowired
    private RegistrationService registrationService;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullRegistrationRequestPassed() throws Exception {
        // when
        registrationService.register(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldThrowRegistrationException_whenUsernameIsNull() throws Exception {
        // given
        RegistrationRequestImpl request = correctRegistrationRequest();
        request.setUsername(null);

        // when
        try {
            registrationService.register(request);
        } catch (RegistrationException e) {
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

            // then
            assertThat(constraintViolations).hasSize(1);
            assertThat(constraintViolations.iterator().next().getPropertyPath().toString()).isEqualTo("username.value");
            return;
        }
        fail("Should throw when username is null");
    }

    @Test
    public void shouldThrowRegistrationException_whenUsernameIsShorterThanThree() throws Exception {
        // given
        RegistrationRequestImpl request = correctRegistrationRequest();
        request.setUsername("cz");

        // when
        try {
            registrationService.register(request);
        } catch (RegistrationException e) {
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

            // then
            assertThat(constraintViolations).hasSize(1);
            assertThat(constraintViolations.iterator().next().getPropertyPath().toString()).isEqualTo("username.value");
            return;
        }
        fail("Should throw when username is shorter than 3");
    }

    @Test
    public void shouldThrowRegistrationException_whenUsernameIsLongerThanTwenty() throws Exception {
        // given
        RegistrationRequestImpl request = correctRegistrationRequest();
        request.setUsername("123456789012345678901");

        // when
        try {
            registrationService.register(request);
        } catch (RegistrationException e) {
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

            // then
            assertThat(constraintViolations).hasSize(1);
            assertThat(constraintViolations.iterator().next().getPropertyPath().toString()).isEqualTo("username.value");
            return;
        }
        fail("Should throw when username is longer than 20");
    }

    @Test
    public void shouldThrowRegistrationException_whenDisplayedNameIsNull() throws Exception {
        // given
        RegistrationRequestImpl request = correctRegistrationRequest();
        request.setDisplayedName(null);

        // when
        try {
            registrationService.register(request);
        } catch (RegistrationException e) {
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

            // then
            assertThat(constraintViolations).hasSize(1);
            assertThat(constraintViolations.iterator().next().getPropertyPath().toString()).isEqualTo("displayedName.value");
            return;
        }
        fail("Should throw when displayed name is null");
    }

    @Test
    public void shouldThrowRegistrationException_whenDisplayedNameIsShorterThanThree() throws Exception {
        // given
        RegistrationRequestImpl request = correctRegistrationRequest();
        request.setDisplayedName("ab");

        // when
        try {
            registrationService.register(request);
        } catch (RegistrationException e) {
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

            // then
            assertThat(constraintViolations).hasSize(1);
            assertThat(constraintViolations.iterator().next().getPropertyPath().toString()).isEqualTo("displayedName.value");
            return;
        }
        fail("Should throw when displayed name is shorter than 3");
    }

    @Test
    public void shouldThrowRegistrationException_whenUsernameIsLongerThan64() throws Exception {
        // given
        RegistrationRequestImpl request = correctRegistrationRequest();
        request.setDisplayedName("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij12345");

        // when
        try {
            registrationService.register(request);
        } catch (RegistrationException e) {
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

            // then
            assertThat(constraintViolations).hasSize(1);
            assertThat(constraintViolations.iterator().next().getPropertyPath().toString()).isEqualTo("displayedName.value");
            return;
        }
        fail("Should throw when displayed name is longer than 64");
    }

    @Test
    public void shouldThrowRegistrationException_whenEmailIsNull() throws Exception {
        // given
        RegistrationRequestImpl request = correctRegistrationRequest();
        request.setEmail(null);

        // when
        try {
            registrationService.register(request);
        } catch (RegistrationException e) {
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

            // then
            assertThat(constraintViolations).hasSize(1);
            assertThat(constraintViolations.iterator().next().getPropertyPath().toString()).isEqualTo("email.value");
            return;
        }
        fail("Should throw when email is null");
    }

    @Test
    public void shouldThrowRegistrationException_whenEmailIsIncorrect() throws Exception {
        // given
        RegistrationRequestImpl request = correctRegistrationRequest();
        request.setEmail("this is not an email!");

        // when
        try {
            registrationService.register(request);
        } catch (RegistrationException e) {
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

            // then
            assertThat(constraintViolations).hasSize(1);
            assertThat(constraintViolations.iterator().next().getPropertyPath().toString()).isEqualTo("email.value");
            return;
        }
        fail("Should throw when email is incorrect");
    }

    @Test
    public void shouldThrowRegistrationException_whenIpIsNull() throws Exception {
        // given
        RegistrationRequestImpl request = correctRegistrationRequest();
        request.setIpAddress(null);

        // when
        try {
            registrationService.register(request);
        } catch (RegistrationException e) {
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

            // then
            assertThat(constraintViolations).hasSize(1);
            assertThat(constraintViolations.iterator().next().getPropertyPath().toString())
                    .isEqualTo("registrationMetaData.ipAddress.value");
            return;
        }
        fail("Should throw when IP Address is null");
    }

    private RegistrationRequestImpl correctRegistrationRequest() {
        RegistrationRequestImpl request = new RegistrationRequestImpl();
        request.setUsername("john");
        request.setDisplayedName("John");
        request.setEmail("john@john.com");
        request.setIpAddress("127.0.0.1");
        request.setPassword("P@ssw0rd");
        request.setPasswordAgain("P@ssw0rd");
        return request;
    }
}