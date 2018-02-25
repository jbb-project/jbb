/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc.security;

import org.jbb.lib.commons.JbbBeanSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RootAuthFailureHandler implements AuthenticationFailureHandler {

    private final List<? extends AuthenticationFailureHandler> handlers;

    @Autowired
    public RootAuthFailureHandler(JbbBeanSearch jbbBeanSearch) {
        handlers = jbbBeanSearch.getBeanClasses(AuthenticationFailureHandler.class);
        handlers.removeIf(handler -> handler.getClass().equals(RootAuthFailureHandler.class));
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        AuthenticationException e) throws IOException, ServletException {
        handlers.stream()
                .forEach(handler -> {
                    try {
                        handler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                    } catch (IOException | ServletException e1) { //NOSONAR
                        log.error("Error during authentication failure", e1);
                        throw new IllegalStateException(e1);
                    }
                });
    }

}
