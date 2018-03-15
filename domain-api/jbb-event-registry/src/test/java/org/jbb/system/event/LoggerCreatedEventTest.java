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

public class LoggerCreatedEventTest extends BaseEventTest {

    @Test
    public void shouldSetLoggerName() {
        // given
        String expectedLoggerName = "aaa";
        LoggerCreatedEvent event = new LoggerCreatedEvent(expectedLoggerName);

        // when
        eventBus.post(event);
        String loggerName = event.getLoggerName();

        // then
        assertThat(loggerName).isEqualTo(expectedLoggerName);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullLoggerNamePassed() {
        // given
        String nullName = null;
        LoggerCreatedEvent event = new LoggerCreatedEvent(nullName);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenEmptyLoggerNamePassed() {
        // given
        String emptyName = StringUtils.EMPTY;
        LoggerCreatedEvent event = new LoggerCreatedEvent(emptyName);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenBlankLoggerNamePassed() {
        // given
        String blankName = StringUtils.SPACE;
        LoggerCreatedEvent event = new LoggerCreatedEvent(blankName);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}