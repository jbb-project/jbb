/*
 * Copyright (C) 2017 the original author or authors.
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

public class LoggerRemovedEventTest extends BaseEventTest {

    @Test
    public void shouldSetLoggerName() throws Exception {
        // given
        String expectedLoggerName = "aaa";
        LoggerRemovedEvent event = new LoggerRemovedEvent(expectedLoggerName);

        // when
        eventBus.post(event);
        String loggerName = event.getLoggerName();

        // then
        assertThat(loggerName).isEqualTo(expectedLoggerName);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullLoggerNamePassed() throws Exception {
        // given
        String nullName = null;
        LoggerRemovedEvent event = new LoggerRemovedEvent(nullName);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenEmptyLoggerNamePassed() throws Exception {
        // given
        String emptyName = StringUtils.EMPTY;
        LoggerRemovedEvent event = new LoggerRemovedEvent(emptyName);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenBlankLoggerNamePassed() throws Exception {
        // given
        String blankName = StringUtils.SPACE;
        LoggerRemovedEvent event = new LoggerRemovedEvent(blankName);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}