/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.logic.password;

import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.core.vo.Login;
import org.jbb.lib.core.vo.Password;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CleanHsqlDbAfterTestsConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.security.MemberConfigMocks;
import org.jbb.security.api.data.PasswordRequirements;
import org.jbb.security.api.exception.PasswordException;
import org.jbb.security.api.service.PasswordService;
import org.jbb.security.impl.SecurityConfig;
import org.jbb.security.impl.password.logic.PasswordLengthRequirements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfigMocks.class, CleanHsqlDbAfterTestsConfig.class,
        SecurityConfig.class, PropertiesConfig.class,
        EventBusConfig.class, DbConfig.class, MemberConfigMocks.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class PasswordServiceIT {
    @Autowired
    private PasswordService passwordService;

    @Autowired
    private PasswordLengthRequirements passwordRequirements;

    @Test
    public void shouldVerificationFailed_afterPasswordChanging() throws Exception {
        // given
        Login login = Login.builder().value("john").build();

        Password password = Password.builder().value("foobar".toCharArray()).build();
        Password typedPassword = Password.builder().value("foobar".toCharArray()).build();
        Password newPassword = Password.builder().value("winamp".toCharArray()).build();

        // when
        passwordService.changeFor(login, password);
        boolean verification = passwordService.verifyFor(login, typedPassword);

        passwordService.changeFor(login, newPassword);
        boolean secondVerification = passwordService.verifyFor(login, typedPassword);

        // then
        assertThat(verification).isTrue();
        assertThat(secondVerification).isFalse();
    }

    @Test
    public void currentPasswordRequirementsShouldBeAvailableAsSpringBean() throws Exception {
        // when
        PasswordRequirements currentPasswordRequirements = passwordService.currentRequirements();

        // then
        assertThat(currentPasswordRequirements).isEqualTo(passwordRequirements);
    }

    @Test(expected = PasswordException.class)
    public void shouldThrowPasswordException_whenPasswordIsTooShort() throws Exception {
        // given
        TestbedPasswordRequirements requirements = new TestbedPasswordRequirements();
        requirements.setMinimumLength(4);
        requirements.setMaximumLength(16);

        Login login = Login.builder().value("john").build();
        Password password = Password.builder().value("foo".toCharArray()).build();

        // when
        passwordService.updateRequirements(requirements);
        passwordService.changeFor(login, password);

        // then
        // throw PasswordException
    }

    @Test(expected = PasswordException.class)
    public void shouldThrowPasswordException_whenPasswordIsTooLong() throws Exception {
        // given
        TestbedPasswordRequirements requirements = new TestbedPasswordRequirements();
        requirements.setMinimumLength(4);
        requirements.setMaximumLength(16);

        Login login = Login.builder().value("john").build();
        Password password = Password.builder().value("12345678901234567".toCharArray()).build();

        // when
        passwordService.updateRequirements(requirements);
        passwordService.changeFor(login, password);

        // then
        // throw PasswordException
    }

    @Test(expected = PasswordException.class)
    public void shouldNotPermitToUseEmptyPassword() throws Exception {
        // given
        Login login = Login.builder().value("john").build();
        Password emptyPassword = Password.builder().value(StringUtils.EMPTY.toCharArray()).build();

        // when
        passwordService.changeFor(login, emptyPassword);

        // then
        // throw PasswordException
    }

    @Test
    public void shouldPermitOneSignLengthPassword_whenMinimumLengthIsNotSet() throws Exception {
        // given
        TestbedPasswordRequirements requirements = new TestbedPasswordRequirements();
        requirements.setMinimumLength(0);
        requirements.setMaximumLength(16);

        Login login = Login.builder().value("john").build();
        Password password = Password.builder().value("a".toCharArray()).build();

        // when
        passwordService.updateRequirements(requirements);
        passwordService.changeFor(login, password);

        boolean verification = passwordService.verifyFor(login, password);

        // then
        assertThat(verification).isTrue();
    }

    @Test(expected = PasswordException.class)
    public void shouldDenyToChangeToTheSamePassword_whenMinimumLengthRequirementIncreased() throws Exception {
        // given
        TestbedPasswordRequirements requirements = new TestbedPasswordRequirements();
        requirements.setMinimumLength(4);
        requirements.setMaximumLength(16);

        Login login = Login.builder().value("john").build();
        Password password = Password.builder().value("abcd".toCharArray()).build();

        // when
        passwordService.updateRequirements(requirements);
        passwordService.changeFor(login, password);

        assertThat(passwordService.verifyFor(login, password)).isTrue();

        requirements.setMinimumLength(8);
        passwordService.updateRequirements(requirements);

        passwordService.changeFor(login, password);

        // then
        // throw PasswordException
    }

    private class TestbedPasswordRequirements implements PasswordRequirements {

        private int minimumLength = 0;
        private int maximumLength = 0;

        @Override
        public Optional<Integer> minimumLength() {
            return minimumLength > 0 ? Optional.of(minimumLength) : Optional.empty();
        }

        @Override
        public Optional<Integer> maximumLength() {
            return maximumLength > 0 ? Optional.of(maximumLength) : Optional.empty();
        }

        public void setMinimumLength(int minimumLength) {
            this.minimumLength = minimumLength;
        }

        public void setMaximumLength(int maximumLength) {
            this.maximumLength = maximumLength;
        }
    }
}