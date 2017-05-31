/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.event;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SignInSuccessEventTest {
    @Test
    public void shouldSetUsername() throws Exception {
        // given
        Long expectedId = 33L;
        SignInSuccessEvent event = new SignInSuccessEvent(expectedId);

        // when
        Long id = event.getMemberId();

        // then
        assertThat(id).isEqualTo(expectedId);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullMemberIdPassed() throws Exception {
        // given
        Long nullId = null;

        // when
        new SignInSuccessEvent(nullId);

        // then
        // throw NullPointerException
    }

}