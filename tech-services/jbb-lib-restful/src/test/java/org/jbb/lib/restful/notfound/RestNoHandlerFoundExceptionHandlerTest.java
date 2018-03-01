/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.restful.notfound;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class RestNoHandlerFoundExceptionHandlerTest {

    private RestNoHandlerFoundExceptionHandler restNoHandlerFoundExceptionHandler;

    @Before
    public void setUp() {
        restNoHandlerFoundExceptionHandler = new RestNoHandlerFoundExceptionHandler(new ObjectMapper());
    }

    @Test
    public void shouldReturnOptionalEmpty_whenRequestURL_doesNotStartWithApi() {
        // given
        NoHandlerFoundException ex = new NoHandlerFoundException("POST", "/acp", null);

        // when
        Optional<ModelAndView> modelAndView = restNoHandlerFoundExceptionHandler.handle(ex);

        // then
        assertThat(modelAndView).isNotPresent();
    }

    @Test
    public void shouldReturnErrorResponseModel_whenRequestURL_startsWithApi() {
        // given
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/api/members/11", null);

        // when
        Optional<ModelAndView> modelAndView = restNoHandlerFoundExceptionHandler.handle(ex);

        // then
        assertThat(modelAndView).isPresent();
        assertThat(modelAndView.get().getModel()).containsKey("errorResponse");
    }

}