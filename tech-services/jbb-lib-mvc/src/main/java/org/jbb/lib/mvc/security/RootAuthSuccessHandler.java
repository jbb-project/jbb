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
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RootAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final List<? extends AuthenticationSuccessHandler> handlers;

    @Autowired
    public RootAuthSuccessHandler(JbbBeanSearch jbbBeanSearch) {
        handlers = jbbBeanSearch.getBeanClasses(AuthenticationSuccessHandler.class);
        handlers.removeIf(handler -> handler.getClass().equals(RootAuthSuccessHandler.class));
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        handlers.stream()
                .forEach(handler -> {
                    try {
                        handler.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
                    } catch (IOException | ServletException e) { //NOSONAR
                        log.error("Error during authentication success", e);
                        throw new IllegalArgumentException(e);
                    }
                });
    }

}
