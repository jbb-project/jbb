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

public class PasswordChangedEventTest {
    @Test
    public void shouldSetMemberId() throws Exception {
        // given
        Long expectedId = 23L;
        PasswordChangedEvent event = new PasswordChangedEvent(expectedId);

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
        new PasswordChangedEvent(nullId);

        // then
        // throw NullPointerException
    }

}