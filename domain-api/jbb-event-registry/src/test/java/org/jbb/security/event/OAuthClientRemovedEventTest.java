/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.event;

import org.jbb.BaseEventTest;
import org.jbb.lib.eventbus.EventValidationException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuthClientRemovedEventTest extends BaseEventTest {

    @Test
    public void shouldSetClientId() {
        // given
        String expectedClientId = "service_xydhrcd";
        OAuthClientRemovedEvent event = new OAuthClientRemovedEvent(expectedClientId);

        // when
        eventBus.post(event);
        String clientId = event.getClientId();

        // then
        assertThat(clientId).isEqualTo(expectedClientId);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullClientIdPassed() {
        // given
        String nullId = null;
        OAuthClientRemovedEvent event = new OAuthClientRemovedEvent(nullId);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}