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

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Lists;
import java.util.List;
import org.jbb.BaseEventTest;
import org.jbb.lib.eventbus.EventValidationException;
import org.junit.Test;

public class TopicRemovedEventTest extends BaseEventTest {

    @Test
    public void shouldSetTopicId_andRemovedPostsIds() {
        // given
        Long expectedId = 344L;
        List<Long> expectedPostsIds = Lists.newArrayList(22L);
        TopicRemovedEvent event = new TopicRemovedEvent(expectedId, expectedPostsIds);

        // when
        eventBus.post(event);
        Long topicId = event.getTopicId();
        List<Long> removedPostsIds = event.getRemovedPostsIds();

        // then
        assertThat(topicId).isEqualTo(expectedId);
        assertThat(removedPostsIds).isEqualTo(expectedPostsIds);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullTopicIdPassed() {
        // given
        Long nullId = null;
        List<Long> postsIds = Lists.newArrayList();
        TopicRemovedEvent event = new TopicRemovedEvent(nullId, postsIds);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullRemovedPostsIdsPassed() {
        // given
        Long id = 88L;
        List<Long> nullPostsIds = null;
        TopicRemovedEvent event = new TopicRemovedEvent(id, nullPostsIds);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}