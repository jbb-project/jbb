/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc;

import org.junit.After;
import org.junit.Test;
import org.slf4j.MDC;

import javax.servlet.ServletRequestEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class RequestIdListenerTest {

    @After
    public void tearDown() throws Exception {
        MDC.clear();
    }

    @Test
    public void shouldPutRequestId_whenRequestInitialized() throws Exception {
        // given
        RequestIdListener requestIdListener = new RequestIdListener();

        // when
        requestIdListener.requestInitialized(mock(ServletRequestEvent.class));

        // then
        assertThat(MDC.get("RequestId")).isNotBlank();
    }

    @Test
    public void shouldRemoveRequestId_whenRequestInitialized() throws Exception {
        // given
        MDC.put("RequestId", "request-id");
        RequestIdListener requestIdListener = new RequestIdListener();

        // when
        requestIdListener.requestDestroyed(mock(ServletRequestEvent.class));

        // then
        assertThat(MDC.get("RequestId")).isNull();
    }
}