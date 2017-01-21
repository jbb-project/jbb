/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password.logic;

import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.core.vo.Password;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CleanHsqlDbAfterTestsConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.members.event.MemberRemovedEvent;
import org.jbb.security.api.data.PasswordRequirements;
import org.jbb.security.api.exception.PasswordException;
import org.jbb.security.api.service.PasswordService;
import org.jbb.security.impl.MemberConfigMocks;
import org.jbb.security.impl.SecurityConfig;
import org.jbb.security.impl.password.dao.PasswordRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfigMocks.class, CleanHsqlDbAfterTestsConfig.class,
        SecurityConfig.class, PropertiesConfig.class,
        EventBusConfig.class, DbConfig.class, MemberConfigMocks.class})
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
public class PasswordServiceIT {
    @Autowired
    private PasswordService passwordService;

    @Autowired
    private PasswordLengthRequirements passwordRequirements;

    @Autowired
    private JbbEventBus eventBus;

    @Autowired
    private PasswordRepository passwordRepository;

    @After
    public void tearDown() throws Exception {
        passwordRepository.deleteAll();
    }

    @Test
    public void shouldRemovePasswordEntity_afterReceivingMemberRemovedEvent() throws Exception {
        // given
        Long memberId = 1233L;
        Password password = Password.builder().value("foobarba".toCharArray()).build();
        passwordService.changeFor(memberId, password);

        // when
        eventBus.post(new MemberRemovedEvent(memberId));

        // then
        assertThat(passwordRepository.count()).isZero();
    }

    @Test
    public void shouldVerificationFailed_afterPasswordChanging() throws Exception {
        // given
        Long memberId = 233L;

        Password password = Password.builder().value("foobar".toCharArray()).build();
        Password typedPassword = Password.builder().value("foobar".toCharArray()).build();
        Password newPassword = Password.builder().value("winamp".toCharArray()).build();

        // when
        passwordService.changeFor(memberId, password);
        boolean verification = passwordService.verifyFor(memberId, typedPassword);

        passwordService.changeFor(memberId, newPassword);
        boolean secondVerification = passwordService.verifyFor(memberId, typedPassword);

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

        Long memberId = 233L;
        Password password = Password.builder().value("foo".toCharArray()).build();

        // when
        passwordService.updateRequirements(requirements);
        passwordService.changeFor(memberId, password);

        // then
        // throw PasswordException
    }

    @Test(expected = PasswordException.class)
    public void shouldThrowPasswordException_whenPasswordIsTooLong() throws Exception {
        // given
        TestbedPasswordRequirements requirements = new TestbedPasswordRequirements();
        requirements.setMinimumLength(4);
        requirements.setMaximumLength(16);

        Long memberId = 233L;
        Password password = Password.builder().value("12345678901234567".toCharArray()).build();

        // when
        passwordService.updateRequirements(requirements);
        passwordService.changeFor(memberId, password);

        // then
        // throw PasswordException
    }

    @Test(expected = PasswordException.class)
    public void shouldNotPermitToUseEmptyPassword() throws Exception {
        // given
        Long memberId = 233L;
        Password emptyPassword = Password.builder().value(StringUtils.EMPTY.toCharArray()).build();

        // when
        passwordService.changeFor(memberId, emptyPassword);

        // then
        // throw PasswordException
    }

    @Test
    public void shouldPermitOneSignLengthPassword_whenMinimumLengthIsOne() throws Exception {
        // given
        TestbedPasswordRequirements requirements = new TestbedPasswordRequirements();
        requirements.setMinimumLength(1);
        requirements.setMaximumLength(16);

        Long memberId = 233L;
        Password password = Password.builder().value("a".toCharArray()).build();

        // when
        passwordService.updateRequirements(requirements);
        passwordService.changeFor(memberId, password);

        boolean verification = passwordService.verifyFor(memberId, password);

        // then
        assertThat(verification).isTrue();
    }

    @Test(expected = PasswordException.class)
    public void shouldDenyToChangeToTheSamePassword_whenMinimumLengthRequirementIncreased() throws Exception {
        // given
        TestbedPasswordRequirements requirements = new TestbedPasswordRequirements();
        requirements.setMinimumLength(4);
        requirements.setMaximumLength(16);

        Long memberId = 233L;
        Password password = Password.builder().value("abcd".toCharArray()).build();

        // when
        passwordService.updateRequirements(requirements);
        passwordService.changeFor(memberId, password);

        assertThat(passwordService.verifyFor(memberId, password)).isTrue();

        requirements.setMinimumLength(8);
        passwordService.updateRequirements(requirements);

        passwordService.changeFor(memberId, password);

        // then
        // throw PasswordException
    }

    private class TestbedPasswordRequirements implements PasswordRequirements {

        private int minimumLength = 1;
        private int maximumLength = Integer.MAX_VALUE;

        @Override
        public int minimumLength() {
            return minimumLength;
        }

        @Override
        public int maximumLength() {
            return maximumLength;
        }

        public void setMinimumLength(int minimumLength) {
            this.minimumLength = minimumLength;
        }

        public void setMaximumLength(int maximumLength) {
            this.maximumLength = maximumLength;
        }
    }
}