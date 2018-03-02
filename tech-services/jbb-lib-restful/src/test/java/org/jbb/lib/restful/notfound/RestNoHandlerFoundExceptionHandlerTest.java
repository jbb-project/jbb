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

import org.jbb.lib.mvc.PathResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class RestNoHandlerFoundExceptionHandlerTest {

    @Mock
    private PathResolver pathResolverMock;

    @Mock
    private ObjectMapper objectMapperMock;

    @InjectMocks
    private RestNoHandlerFoundExceptionHandler restNoHandlerFoundExceptionHandler;

    @Test
    public void shouldReturnOptionalEmpty_whenRequest_isNotToApi() {
        // given
        given(pathResolverMock.isRequestToApi()).willReturn(false);
        NoHandlerFoundException ex = new NoHandlerFoundException("POST", "/acp", null);

        // when
        Optional<ModelAndView> modelAndView = restNoHandlerFoundExceptionHandler.handle(ex);

        // then
        assertThat(modelAndView).isNotPresent();
    }

    @Test
    public void shouldReturnErrorResponseModel_whenRequest_isToApi() {
        // given
        given(pathResolverMock.isRequestToApi()).willReturn(true);
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/api/members/11", null);

        // when
        Optional<ModelAndView> modelAndView = restNoHandlerFoundExceptionHandler.handle(ex);

        // then
        assertThat(modelAndView).isPresent();
        assertThat(modelAndView.get().getModel()).containsKey("errorResponse");
    }

}