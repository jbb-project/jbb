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

import org.jbb.BaseEventTest;
import org.jbb.lib.eventbus.EventValidationException;
import org.junit.Test;

public class PostChangedEventTest extends BaseEventTest {

    @Test
    public void shouldSetPostId() {
        // given
        Long expectedId = 344L;
        PostChangedEvent event = new PostChangedEvent(expectedId);

        // when
        eventBus.post(event);
        Long postId = event.getPostId();

        // then
        assertThat(postId).isEqualTo(expectedId);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullPostIdPassed() {
        // given
        Long nullId = null;
        PostChangedEvent event = new PostChangedEvent(nullId);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}