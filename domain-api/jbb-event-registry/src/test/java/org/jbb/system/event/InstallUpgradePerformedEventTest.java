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

public class InstallUpgradePerformedEventTest extends BaseEventTest {

    @Test
    public void shouldSetJbbVersion() {
        // given
        String expectedJbbVersion = "0.11.5";
        InstallUpgradePerformedEvent event = new InstallUpgradePerformedEvent(expectedJbbVersion);

        // when
        eventBus.post(event);
        String jbbVersion = event.getJbbVersion();

        // then
        assertThat(jbbVersion).isEqualTo(expectedJbbVersion);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullJbbVersionPassed() {
        // given
        String nullJbbVersion = null;
        InstallUpgradePerformedEvent event = new InstallUpgradePerformedEvent(nullJbbVersion);
        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenEmptyJbbVersionPassed() {
        // given
        String emptyJbbVersion = StringUtils.EMPTY;
        InstallUpgradePerformedEvent event = new InstallUpgradePerformedEvent(emptyJbbVersion);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenBlankJbbVersionPassed() {
        // given
        String blankJbbVersion = StringUtils.SPACE;
        InstallUpgradePerformedEvent event = new InstallUpgradePerformedEvent(blankJbbVersion);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}