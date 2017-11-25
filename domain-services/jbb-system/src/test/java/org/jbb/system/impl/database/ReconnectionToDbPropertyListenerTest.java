/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.beans.PropertyChangeEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReconnectionToDbPropertyListenerTest {

    @Mock
    private DatabaseSettingsManager eventSenderMock;

    @InjectMocks
    private ReconnectionToDbPropertyListener reconnectionToDbPropertyListener;

    @Test
    public void shouldEmitEvent() throws Exception {
        // when
        reconnectionToDbPropertyListener.propertyChange(mock(PropertyChangeEvent.class));

        // then
        verify(eventSenderMock).triggerRefresh();
    }
}