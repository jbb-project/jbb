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

public class AdministratorRoleAddedEventTest {

    @Test
    public void shouldSetMemberId() throws Exception {
        // given
        Long expectedId = 1234L;
        AdministratorRoleAddedEvent event = new AdministratorRoleAddedEvent(expectedId);

        // when
        Long memberId = event.getMemberId();

        // then
        assertThat(memberId).isEqualTo(expectedId);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullIdPassed() throws Exception {
        // given
        Long nullId = null;

        // when
        new AdministratorRoleAddedEvent(nullId);

        // then
        // throw NullPointerException
    }
}