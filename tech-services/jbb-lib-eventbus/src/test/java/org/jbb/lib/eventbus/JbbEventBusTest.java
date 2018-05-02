/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.eventbus;

import com.google.common.eventbus.Subscribe;

import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EventBusConfig.class, CommonsConfig.class,
        MockCommonsConfig.class})
// starting up this context is cheap so it is unit test not IT
public class JbbEventBusTest {
    @Autowired
    private JbbEventBus eventBus;

    private boolean listenerConsumedEvent = false;
    private boolean failingListenerConsumedEvent = false;

    @Test
    public void listenerShouldConsumeEvent() {
        eventBus.register(new ExampleEventListener());

        eventBus.post(new ExampleEvent(4));

        assertThat(listenerConsumedEvent).isTrue();
    }

    @Test
    public void failingListenerShouldConsumeEvent_andPublishedShouldNotBeAwareOfFail() {
        eventBus.register(new ExampleEventFailingListener());

        eventBus.post(new ExampleEvent(4));

        assertThat(failingListenerConsumedEvent).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenNotJbbEventPassedToPost() {
        // when
        eventBus.post(new Object());

        // then
        // throw IllegalArgumentException
    }

    private class ExampleEvent extends JbbEvent {
        private int value;

        public ExampleEvent(int value) {
            this.value = value;
        }
    }

    private class ExampleEventListener {
        @Subscribe
        public void foo(ExampleEvent exampleEvent) {
            listenerConsumedEvent = true;
            assertThat(exampleEvent).isNotNull();
            assertThat(exampleEvent.getEventId()).isNotNull();
            assertThat(exampleEvent.value).isEqualTo(4);
        }
    }

    private class ExampleEventFailingListener {
        @Subscribe
        public void bar(ExampleEvent exampleEvent) {
            failingListenerConsumedEvent = true;
            assertThat(exampleEvent).isNotNull();
            assertThat(exampleEvent.getEventId()).isNotNull();
            assertThat(exampleEvent.value).isEqualTo(4);
            throw new IllegalStateException();
        }
    }
}