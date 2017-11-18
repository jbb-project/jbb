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
import static org.junit.Assert.fail;

import java.util.Set;
import javax.validation.ConstraintViolation;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.IPAddress;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.registration.RegistrationException;
import org.jbb.members.api.registration.RegistrationRequest;
import org.jbb.members.api.registration.RegistrationService;
import org.jbb.members.impl.MembersConfig;
import org.jbb.members.impl.SecurityConfigMocks;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonsConfig.class, MockCommonsConfig.class, SecurityConfigMocks.class,
        MembersConfig.class, PropertiesConfig.class,
        EventBusConfig.class, DbConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RegistrationServiceValidationIT {
    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private MemberRepository memberRepository;

    @Before
    public void setUp() throws Exception {
        memberRepository.deleteAll();
    }

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
        RegistrationRequest request = correctRegistrationRequest();
        request.setUsername(null);

        // when
        try {
            registrationService.register(request);
        } catch (RegistrationException e) {
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

            // then
            assertThat(constraintViolations).hasSize(1);
            assertThat(constraintViolations.iterator().next().getPropertyPath().toString())
                .isEqualTo("username");
            return;
        }
        fail("Should throw when username is null");
    }

    @Test
    public void shouldThrowRegistrationException_whenUsernameHasEmptyValue() throws Exception {
        // given
        RegistrationRequest request = correctRegistrationRequest();
        request.setUsername(Username.builder().build());

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
        RegistrationRequest request = correctRegistrationRequest();
        request.setUsername(Username.builder().value("cz").build());

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
        RegistrationRequest request = correctRegistrationRequest();
        request.setUsername(Username.builder().value("123456789012345678901").build());

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
        RegistrationRequest request = correctRegistrationRequest();
        request.setDisplayedName(null);

        // when
        try {
            registrationService.register(request);
        } catch (RegistrationException e) {
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

            // then
            assertThat(constraintViolations).hasSize(1);
            assertThat(constraintViolations.iterator().next().getPropertyPath().toString())
                .isEqualTo("displayedName");
            return;
        }
        fail("Should throw when displayed name is null");
    }

    @Test
    public void shouldThrowRegistrationException_whenDisplayedNameValueIsEmpty() throws Exception {
        // given
        RegistrationRequest request = correctRegistrationRequest();
        request.setDisplayedName(DisplayedName.builder().build());

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
        RegistrationRequest request = correctRegistrationRequest();
        request.setDisplayedName(DisplayedName.builder().value("ab").build());

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
        RegistrationRequest request = correctRegistrationRequest();
        request.setDisplayedName(DisplayedName.builder()
            .value("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij12345").build());

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
        RegistrationRequest request = correctRegistrationRequest();
        request.setEmail(null);

        // when
        try {
            registrationService.register(request);
        } catch (RegistrationException e) {
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

            // then
            assertThat(constraintViolations).hasSize(1);
            assertThat(constraintViolations.iterator().next().getPropertyPath().toString())
                .isEqualTo("email");
            return;
        }
        fail("Should throw when email is null");
    }

    @Test
    public void shouldThrowRegistrationException_whenEmailValueIsEmpty() throws Exception {
        // given
        RegistrationRequest request = correctRegistrationRequest();
        request.setEmail(Email.builder().build());

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
        RegistrationRequest request = correctRegistrationRequest();
        request.setEmail(Email.builder().value("this is not an email!").build());

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
        RegistrationRequest request = correctRegistrationRequest();
        request.setIpAddress(null);

        // when
        try {
            registrationService.register(request);
        } catch (RegistrationException e) {
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

            // then
            assertThat(constraintViolations).hasSize(1);
            assertThat(constraintViolations.iterator().next().getPropertyPath().toString())
                .isEqualTo("registrationMetaData.ipAddress");
            return;
        }
        fail("Should throw when IP Address is null");
    }

    @Test
    public void shouldThrowRegistrationException_whenIpValueIsEmpty() throws Exception {
        // given
        RegistrationRequest request = correctRegistrationRequest();
        request.setIpAddress(IPAddress.builder().build());

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

    private RegistrationRequest correctRegistrationRequest() {
        return RegistrationRequest.builder()
            .username(Username.builder().value("john").build())
            .displayedName(DisplayedName.builder().value("John").build())
            .email(Email.builder().value("john@john.com").build())
            .password(Password.builder().value("P@ssw0rd".toCharArray()).build())
            .passwordAgain(Password.builder().value("P@ssw0rd".toCharArray()).build())
            .ipAddress(IPAddress.builder().value("127.0.0.1").build())
            .build();
    }
}