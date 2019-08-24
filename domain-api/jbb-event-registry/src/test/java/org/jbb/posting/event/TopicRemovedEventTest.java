/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.event;

import org.jbb.BaseEventTest;
import org.jbb.lib.eventbus.EventValidationException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TopicRemovedEventTest extends BaseEventTest {

    @Test
    public void shouldSetTopicId() {
        // given
        Long expectedId = 344L;
        TopicRemovedEvent event = new TopicRemovedEvent(expectedId);

        // when
        eventBus.post(event);
        Long topicId = event.getTopicId();

        // then
        assertThat(topicId).isEqualTo(expectedId);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullTopicIdPassed() {
        // given
        Long nullId = null;
        TopicRemovedEvent event = new TopicRemovedEvent(nullId);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}