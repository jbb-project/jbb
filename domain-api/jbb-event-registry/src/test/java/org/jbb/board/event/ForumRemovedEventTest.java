/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.BaseEventTest;
import org.jbb.lib.eventbus.EventValidationException;
import org.junit.Test;

public class ForumRemovedEventTest extends BaseEventTest {
    @Test
    public void shouldSetForumId() throws Exception {
        // given
        Long expectedId = 344L;
        ForumRemovedEvent event = new ForumRemovedEvent(expectedId);

        // when
        eventBus.post(event);
        Long forumId = event.getForumId();

        // then
        assertThat(forumId).isEqualTo(expectedId);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullForumIdPassed() throws Exception {
        // given
        Long nullId = null;
        ForumRemovedEvent event = new ForumRemovedEvent(nullId);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}