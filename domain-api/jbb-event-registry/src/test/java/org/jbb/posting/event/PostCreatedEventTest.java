/*
 * Copyright (C) 2019 the original author or authors.
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

public class PostCreatedEventTest extends BaseEventTest {

    @Test
    public void shouldSetPostId_andTopicId() {
        // given
        Long expectedPostId = 344L;
        Long expectedTopicId = 222L;
        PostCreatedEvent event = new PostCreatedEvent(expectedPostId, expectedTopicId);

        // when
        eventBus.post(event);
        Long postId = event.getPostId();
        Long topicId = event.getTopicId();

        // then
        assertThat(postId).isEqualTo(expectedPostId);
        assertThat(topicId).isEqualTo(expectedTopicId);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullPostIdPassed() {
        // given
        Long nullId = null;
        Long topicId = 222L;
        PostCreatedEvent event = new PostCreatedEvent(nullId, topicId);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullTopicIdPassed() {
        // given
        Long postId = 222L;
        Long nullId = null;
        PostCreatedEvent event = new PostCreatedEvent(postId, nullId);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}