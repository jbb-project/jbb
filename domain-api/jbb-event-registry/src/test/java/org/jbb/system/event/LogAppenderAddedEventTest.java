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

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.StringUtils;
import org.jbb.BaseEventTest;
import org.jbb.lib.eventbus.EventValidationException;
import org.junit.Test;

public class LogAppenderAddedEventTest extends BaseEventTest {

    @Test
    public void shouldSetLogAppenderName() throws Exception {
        // given
        String expectedLogAppenderName = "aaa";
        LogAppenderAddedEvent event = new LogAppenderAddedEvent(expectedLogAppenderName);

        // when
        eventBus.post(event);
        String logAppenderName = event.getLogAppenderName();

        // then
        assertThat(logAppenderName).isEqualTo(expectedLogAppenderName);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullLogAppenderNamePassed()
        throws Exception {
        // given
        String nullName = null;
        LogAppenderAddedEvent event = new LogAppenderAddedEvent(nullName);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenEmptyLogAppenderNamePassed()
        throws Exception {
        // given
        String emptyName = StringUtils.EMPTY;
        LogAppenderAddedEvent event = new LogAppenderAddedEvent(emptyName);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenBlankLogAppenderNamePassed()
        throws Exception {
        // given
        String blankName = StringUtils.SPACE;
        LogAppenderAddedEvent event = new LogAppenderAddedEvent(blankName);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}