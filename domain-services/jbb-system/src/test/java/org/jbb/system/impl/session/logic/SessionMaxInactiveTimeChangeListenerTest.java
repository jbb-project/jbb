/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.session.logic;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.beans.PropertyChangeEvent;
import org.jbb.lib.mvc.session.JbbSessionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SessionMaxInactiveTimeChangeListenerTest {

    @Mock
    private JbbSessionRepository sessionRepositoryMock;

    @InjectMocks
    private SessionMaxInactiveTimeChangeListener sessionMaxInactiveTimeChangeListener;

    @Test
    public void shouldSetMaxInactiveInternalInRepository_whenPropertyChanged() throws Exception {
        // given
        PropertyChangeEvent event = mock(PropertyChangeEvent.class);
        given(event.getNewValue()).willReturn(13);

        // when
        sessionMaxInactiveTimeChangeListener.propertyChange(event);

        // then
        verify(sessionRepositoryMock, times(1)).setDefaultMaxInactiveInterval(eq(13));
    }
}