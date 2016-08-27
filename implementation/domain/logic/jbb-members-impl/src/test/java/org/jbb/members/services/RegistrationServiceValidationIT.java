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
import org.jbb.members.SecurityConfigMocks;
import org.jbb.members.api.exceptions.RegistrationException;
import org.jbb.members.api.services.RegistrationService;
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
    public void shouldThrowRegistrationException_whenLoginIsNull() throws Exception {
        // given
        org.jbb.members.RegistrationRequestImpl request = correctRegistrationRequest();
        request.setLogin(null);

        // when
        try {
            registrationService.register(request);
        } catch (RegistrationException e) {
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

            // then
            assertThat(constraintViolations).hasSize(1);
            assertThat(constraintViolations.iterator().next().getPropertyPath().toString()).isEqualTo("login.value");
            return;
        }
        fail("Should throw when login is null");
    }

    @Test
    public void shouldThrowRegistrationException_whenLoginIsShorterThanThree() throws Exception {
        // given
        org.jbb.members.RegistrationRequestImpl request = correctRegistrationRequest();
        request.setLogin("cz");

        // when
        try {
            registrationService.register(request);
        } catch (RegistrationException e) {
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

            // then
            assertThat(constraintViolations).hasSize(1);
            assertThat(constraintViolations.iterator().next().getPropertyPath().toString()).isEqualTo("login.value");
            return;
        }
        fail("Should throw when login is shorter than 3");
    }

    @Test
    public void shouldThrowRegistrationException_whenLoginIsLongerThanTwenty() throws Exception {
        // given
        org.jbb.members.RegistrationRequestImpl request = correctRegistrationRequest();
        request.setLogin("123456789012345678901");

        // when
        try {
            registrationService.register(request);
        } catch (RegistrationException e) {
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

            // then
            assertThat(constraintViolations).hasSize(1);
            assertThat(constraintViolations.iterator().next().getPropertyPath().toString()).isEqualTo("login.value");
            return;
        }
        fail("Should throw when login is longer than 20");
    }

    @Test
    public void shouldThrowRegistrationException_whenDisplayedNameIsNull() throws Exception {
        // given
        org.jbb.members.RegistrationRequestImpl request = correctRegistrationRequest();
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
        org.jbb.members.RegistrationRequestImpl request = correctRegistrationRequest();
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
    public void shouldThrowRegistrationException_whenLoginIsLongerThan64() throws Exception {
        // given
        org.jbb.members.RegistrationRequestImpl request = correctRegistrationRequest();
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
        org.jbb.members.RegistrationRequestImpl request = correctRegistrationRequest();
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
        org.jbb.members.RegistrationRequestImpl request = correctRegistrationRequest();
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
        org.jbb.members.RegistrationRequestImpl request = correctRegistrationRequest();
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

    private org.jbb.members.RegistrationRequestImpl correctRegistrationRequest() {
        org.jbb.members.RegistrationRequestImpl request = new org.jbb.members.RegistrationRequestImpl();
        request.setLogin("john");
        request.setDisplayedName("John");
        request.setEmail("john@john.com");
        request.setIpAddress("127.0.0.1");
        request.setPassword("P@ssw0rd");
        request.setPasswordAgain("P@ssw0rd");
        return request;
    }
}