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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RootAuthFailureHandler implements AuthenticationFailureHandler, ApplicationContextAware {

    private final Set<Class<? extends AuthenticationFailureHandler>> handlers;

    private ApplicationContext appContext;

    @Autowired
    public RootAuthFailureHandler(Reflections reflections) {
        handlers = reflections.getSubTypesOf(AuthenticationFailureHandler.class);
        handlers.remove(RootAuthFailureHandler.class);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        AuthenticationException e) throws IOException, ServletException {
        handlers.stream()
                .map(appContext::getBean)
                .forEach(handler -> {
                    try {
                        handler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                    } catch (IOException | ServletException e1) { //NOSONAR
                        log.error("Error during authentication failure", e1);
                        Throwables.propagate(e1);
                    }
                });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }
}
