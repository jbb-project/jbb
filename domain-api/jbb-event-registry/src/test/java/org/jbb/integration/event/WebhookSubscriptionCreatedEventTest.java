/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.event;

import org.jbb.BaseEventTest;
import org.jbb.lib.eventbus.EventValidationException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WebhookSubscriptionCreatedEventTest extends BaseEventTest {

    @Test
    public void shouldSetSubscriptionId_andPost() {
        // given
        Long expectedId = 344L;
        WebhookSubscriptionCreatedEvent event = new WebhookSubscriptionCreatedEvent(expectedId);

        // when
        eventBus.post(event);
        Long subscriptionId = event.getSubscriptionId();

        // then
        assertThat(subscriptionId).isEqualTo(expectedId);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullSubscriptionIdPassed() {
        // given
        Long nullId = null;
        WebhookSubscriptionCreatedEvent event = new WebhookSubscriptionCreatedEvent(nullId);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}