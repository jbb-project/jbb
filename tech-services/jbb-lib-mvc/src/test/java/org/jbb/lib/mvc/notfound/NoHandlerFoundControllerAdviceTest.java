/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc.notfound;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class NoHandlerFoundControllerAdviceTest {

    @Mock
    private NoHandlerFoundExceptionHandler firstHandler;

    @Mock
    private NoHandlerFoundExceptionHandler secondHandler;

    private NoHandlerFoundControllerAdvice noHandlerFoundControllerAdvice;

    @Before
    public void setUp() throws Exception {
        noHandlerFoundControllerAdvice = new NoHandlerFoundControllerAdvice(
                Lists.newArrayList(firstHandler, secondHandler)
        );
    }

    @Test(expected = IllegalStateException.class)
    public void throwIllegalStateException_whenAllhandlersCannotProcessException() {
        // given
        given(firstHandler.handle(any())).willReturn(Optional.empty());
        given(secondHandler.handle(any())).willReturn(Optional.empty());

        // when
        noHandlerFoundControllerAdvice.notFoundExceptionHandler(Mockito.mock(NoHandlerFoundException.class));

        // then
        // throw IllegalStateException
    }

    @Test
    public void shouldReturnModelAndViewFromFirstHandler_ifItCanHandle() {
        // given
        ModelAndView mav = mock(ModelAndView.class);
        given(firstHandler.handle(any())).willReturn(Optional.of(mav));

        // when
        ModelAndView modelAndView = noHandlerFoundControllerAdvice.notFoundExceptionHandler(Mockito.mock(NoHandlerFoundException.class));

        // then
        assertThat(modelAndView).isEqualTo(mav);
        Mockito.verifyZeroInteractions(secondHandler);
    }

    @Test
    public void shouldReturnModelAndViewFromSecondHandler_ifItCanHandle() {
        // given
        ModelAndView mav = mock(ModelAndView.class);
        given(firstHandler.handle(any())).willReturn(Optional.empty());
        given(secondHandler.handle(any())).willReturn(Optional.of(mav));

        // when
        ModelAndView modelAndView = noHandlerFoundControllerAdvice.notFoundExceptionHandler(Mockito.mock(NoHandlerFoundException.class));

        // then
        assertThat(modelAndView).isEqualTo(mav);
        Mockito.verify(firstHandler, times(1)).handle(any(NoHandlerFoundException.class));
    }
}