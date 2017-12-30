/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.stacktrace.logic;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class NoHandlerFoundExceptionHandlerTest {

    @InjectMocks
    private NoHandlerFoundExceptionHandler noHandlerFoundExceptionHandler;

    @Test
    public void shouldReturn404View_whenNoHandlerFoundException() throws Exception {
        // when
        ModelAndView modelAndView = noHandlerFoundExceptionHandler.notFoundExceptionHandler();

        // then
        assertThat(modelAndView.getViewName()).isEqualTo("notFoundException");
    }


}