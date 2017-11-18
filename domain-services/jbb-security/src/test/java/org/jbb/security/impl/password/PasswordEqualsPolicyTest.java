/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import org.jbb.lib.commons.vo.Password;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class PasswordEqualsPolicyTest {
    @Mock
    private PasswordEncoder passwordEncoderMock;

    @InjectMocks
    private PasswordEqualsPolicy passwordEqualsPolicy;

    @Test
    public void shouldPassVerification_whenPasswordHashesMatched() throws Exception {
        // given
        Password typedPassword = Password.builder().value("pass".toCharArray()).build();
        Password encodedCurrentPassword = Password.builder().value("passEncoded".toCharArray()).build();

        given(passwordEncoderMock.matches(eq("pass"), eq("passEncoded"))).willReturn(true);

        // when
        boolean verificationResult = passwordEqualsPolicy.matches(typedPassword, encodedCurrentPassword);

        // then
        assertThat(verificationResult).isTrue();
    }

    @Test
    public void shouldFailVerification_whenPasswordHashesAreDifferent() throws Exception {
        // given
        Password typedPassword = Password.builder().value("incorrectpass".toCharArray()).build();
        Password encodedCurrentPassword = Password.builder().value("passEncoded".toCharArray()).build();

        given(passwordEncoderMock.matches(eq("incorrectpass"), eq("passEncoded"))).willReturn(false);

        // when
        boolean verificationResult = passwordEqualsPolicy.matches(typedPassword, encodedCurrentPassword);

        // then
        assertThat(verificationResult).isFalse();
    }

}