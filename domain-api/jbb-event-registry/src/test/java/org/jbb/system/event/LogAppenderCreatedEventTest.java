/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.event;

import org.apache.commons.lang3.StringUtils;
import org.jbb.BaseEventTest;
import org.jbb.lib.eventbus.EventValidationException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LogAppenderCreatedEventTest extends BaseEventTest {

    @Test
    public void shouldSetLogAppenderName() {
        // given
        String expectedLogAppenderName = "aaa";
        LogAppenderCreatedEvent event = new LogAppenderCreatedEvent(expectedLogAppenderName);

        // when
        eventBus.post(event);
        String logAppenderName = event.getLogAppenderName();

        // then
        assertThat(logAppenderName).isEqualTo(expectedLogAppenderName);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullLogAppenderNamePassed() {
        // given
        String nullName = null;
        LogAppenderCreatedEvent event = new LogAppenderCreatedEvent(nullName);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenEmptyLogAppenderNamePassed() {
        // given
        String emptyName = StringUtils.EMPTY;
        LogAppenderCreatedEvent event = new LogAppenderCreatedEvent(emptyName);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenBlankLogAppenderNamePassed() {
        // given
        String blankName = StringUtils.SPACE;
        LogAppenderCreatedEvent event = new LogAppenderCreatedEvent(blankName);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}