/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.event;

import org.jbb.lib.core.vo.Username;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AdministratorRoleAddedEventTest {

    @Test
    public void shouldSetUsername() throws Exception {
        // given
        Username expectedUsername = Username.builder().value("john").build();
        AdministratorRoleAddedEvent event = new AdministratorRoleAddedEvent(expectedUsername);

        // when
        Username username = event.getUsername();

        // then
        assertThat(username).isEqualTo(expectedUsername);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullUsernamePassed() throws Exception {
        // given
        Username nullUsername = null;

        // when
        new AdministratorRoleAddedEvent(nullUsername);

        // then
        // throw NullPointerException
    }
}