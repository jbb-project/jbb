/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.event;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberRemovedEventTest {
    @Test
    public void shouldSetMemberId() throws Exception {
        // given
        Long expectedId = 344L;
        MemberRemovedEvent event = new MemberRemovedEvent(expectedId);

        // when
        Long memberId = event.getMemberId();

        // then
        assertThat(memberId).isEqualTo(expectedId);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullMemberIdPassed() throws Exception {
        // given
        Long nullId = null;

        // when
        new MemberRemovedEvent(nullId);

        // then
        // throw NullPointerException
    }
}