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

import org.jbb.BaseEventTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseSettingsChangedEventTest extends BaseEventTest {

    @Test
    public void shouldCreate_andPost() throws Exception {
        // given
        DatabaseSettingsChangedEvent event = new DatabaseSettingsChangedEvent();

        // when
        eventBus.post(event);

        // then
        assertThat(event).isNotNull();
    }

}