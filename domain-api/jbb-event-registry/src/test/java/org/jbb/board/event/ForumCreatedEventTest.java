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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ForumCreatedEventTest {
    @Test
    public void shouldSetForumId() throws Exception {
        // given
        Long expectedId = 344L;
        ForumCreatedEvent event = new ForumCreatedEvent(expectedId);

        // when
        Long forumId = event.getForumId();

        // then
        assertThat(forumId).isEqualTo(expectedId);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullForumIdPassed() throws Exception {
        // given
        Long nullId = null;

        // when
        new ForumCreatedEvent(nullId);

        // then
        // throw NullPointerException
    }
}