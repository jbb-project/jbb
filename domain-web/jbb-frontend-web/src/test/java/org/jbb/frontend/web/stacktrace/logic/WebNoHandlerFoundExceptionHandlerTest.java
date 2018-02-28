/*
 * Copyright (C) 2018 the original author or authors.
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
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class WebNoHandlerFoundExceptionHandlerTest {

    @InjectMocks
    private WebNoHandlerFoundExceptionHandler webNoHandlerFoundExceptionHandler;

    @Test
    public void shouldReturn404View_whenRequestURL_doesNotStartWithApi() {
        // given
        NoHandlerFoundException ex = new NoHandlerFoundException("POST", "/acp", null);

        // when
        Optional<ModelAndView> modelAndView = webNoHandlerFoundExceptionHandler.handle(ex);

        // then
        assertThat(modelAndView).isPresent();
        assertThat(modelAndView.get().getViewName()).isEqualTo("notFoundException");
    }

    @Test
    public void shouldReturnOptionalEmpty_whenRequestURL_startsWithApi() {
        // given
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/api/members/11", null);

        // when
        Optional<ModelAndView> modelAndView = webNoHandlerFoundExceptionHandler.handle(ex);

        // then
        assertThat(modelAndView).isNotPresent();
    }


}