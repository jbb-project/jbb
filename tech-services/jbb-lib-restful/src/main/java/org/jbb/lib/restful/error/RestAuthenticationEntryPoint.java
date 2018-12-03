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

import com.google.common.collect.ImmutableMap;

import org.jbb.lib.restful.domain.ErrorInfo;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component("restAuthenticationEntryPoint")
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

    private Map<Class<? extends AuthenticationException>, ErrorInfo> errorInfoMapping =
            ImmutableMap.of(
                    BadCredentialsException.class, ErrorInfo.BAD_CREDENTIALS,
                    LockedException.class, ErrorInfo.MEMBER_HAS_BEEN_LOCKED
            );

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.createFrom(getErrorInfo(e));
        ServerHttpResponse outputMessage = new ServletServerHttpResponse(httpServletResponse);
        outputMessage.setStatusCode(errorResponse.getStatus());
        messageConverter.write(errorResponse, MediaType.APPLICATION_JSON, outputMessage);
    }

    private ErrorInfo getErrorInfo(AuthenticationException e) {
        ErrorInfo errorInfo = Optional.ofNullable(errorInfoMapping.get(e.getClass())).orElse(ErrorInfo.UNAUTHORIZED);
        if (errorInfo == ErrorInfo.UNAUTHORIZED) {
            log.warn("Unexpected authentication error. Rollback error info to generic unauthorized error", e);
        }
        return errorInfo;
    }

}
