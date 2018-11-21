/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.members.event.MemberRemovedEvent;
import org.jbb.security.api.password.PasswordException;
import org.jbb.security.api.password.PasswordPolicy;
import org.jbb.security.api.password.PasswordService;
import org.jbb.security.impl.BaseIT;
import org.jbb.security.impl.password.dao.PasswordRepository;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PasswordServiceIT extends BaseIT {
    @Autowired
    private PasswordService passwordService;

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
    public void currentPasswordPolicyShouldBeAvailableAsSpringBean() throws Exception {
        // when
        PasswordPolicy currentPasswordPolicy = passwordService.currentPolicy();

        // then
        assertThat(currentPasswordPolicy).isNotNull();
    }

    @Test(expected = PasswordException.class)
    public void shouldThrowPasswordException_whenPasswordIsTooShort() throws Exception {
        // given
        PasswordPolicy policy = PasswordPolicy.builder()
                .minimumLength(4)
                .maximumLength(16)
                .build();

        Long memberId = 233L;
        Password password = Password.builder().value("foo".toCharArray()).build();

        // when
        passwordService.updatePolicy(policy);
        passwordService.changeFor(memberId, password);

        // then
        // throw PasswordException
    }

    @Test(expected = PasswordException.class)
    public void shouldThrowPasswordException_whenPasswordIsTooLong() throws Exception {
        // given
        PasswordPolicy policy = PasswordPolicy.builder()
                .minimumLength(4)
                .maximumLength(16)
                .build();

        Long memberId = 233L;
        Password password = Password.builder().value("12345678901234567".toCharArray()).build();

        // when
        passwordService.updatePolicy(policy);
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
        PasswordPolicy policy = PasswordPolicy.builder()
                .minimumLength(1)
                .maximumLength(16)
                .build();

        Long memberId = 233L;
        Password password = Password.builder().value("a".toCharArray()).build();

        // when
        passwordService.updatePolicy(policy);
        passwordService.changeFor(memberId, password);

        boolean verification = passwordService.verifyFor(memberId, password);

        // then
        assertThat(verification).isTrue();
    }

    @Test(expected = PasswordException.class)
    public void shouldDenyToChangeToTheSamePassword_whenMinimumLengthIncreased() throws Exception {
        // given
        PasswordPolicy policy = PasswordPolicy.builder()
                .minimumLength(4)
                .maximumLength(16)
                .build();

        Long memberId = 233L;
        Password password = Password.builder().value("abcd".toCharArray()).build();

        // when
        passwordService.updatePolicy(policy);
        passwordService.changeFor(memberId, password);

        assertThat(passwordService.verifyFor(memberId, password)).isTrue();

        policy.setMinimumLength(8);
        passwordService.updatePolicy(policy);

        passwordService.changeFor(memberId, password);

        // then
        // throw PasswordException
    }

}