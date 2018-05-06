/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.restful.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import org.jbb.lib.commons.web.ClientStackTraceProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

@RunWith(MockitoJUnitRunner.class) //TODO - test another handlers
public class RestExceptionHandlerTest {

    @Mock
    private MessageSource messageSourceMock;

    @Mock
    private ClientStackTraceProvider stacktraceProviderMock;

    @InjectMocks
    private RestExceptionHandler restExceptionHandler;

    @Test
    public void handleGenericException() {
        // given
        given(stacktraceProviderMock.getClientStackTrace(any()))
            .willReturn(Optional.of("stacktrace"));

        // when
        ResponseEntity<Object> responseEntity = restExceptionHandler.handleExceptionInternal(new Exception(), mock(WebRequest.class));

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}