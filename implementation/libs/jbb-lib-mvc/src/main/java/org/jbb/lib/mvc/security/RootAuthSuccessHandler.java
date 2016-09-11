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

import com.google.common.base.Throwables;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RootAuthSuccessHandler implements AuthenticationSuccessHandler, ApplicationContextAware {

    private final Set<Class<? extends AuthenticationSuccessHandler>> handlers;

    private ApplicationContext appContext;

    @Autowired
    public RootAuthSuccessHandler(Reflections reflections) {
        this.handlers = reflections.getSubTypesOf(AuthenticationSuccessHandler.class);
        this.handlers.remove(RootAuthSuccessHandler.class);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        handlers.stream()
                .map(appContext::getBean)
                .forEach(handler -> {
                    try {
                        handler.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
                    } catch (IOException | ServletException e) { //NOSONAR
                        log.error("Error during authentication success", e);
                        Throwables.propagate(e);
                    }
                });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.appContext = applicationContext;
    }
}
