/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.logic.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReplacingViewStrategyTest {
    @Spy
    private ReplacingViewStrategy replacingViewStrategy;

    @Test
    public void shouldPerformHandle_whenCan() throws Exception {
        // given
        when(replacingViewStrategy.canHandle(any())).thenReturn(true);

        // when
        replacingViewStrategy.handle(mock(ModelAndView.class));

        // then
        verify(replacingViewStrategy).performHandle(any());
    }

    @Test
    public void shouldNotPerformHandle_whenCanNot() throws Exception {
        // given
        when(replacingViewStrategy.canHandle(any())).thenReturn(false);

        // when
        replacingViewStrategy.handle(mock(ModelAndView.class));

        // then
        verify(replacingViewStrategy, times(0)).performHandle(any());
    }
}