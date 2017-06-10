/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc.security;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RootLogoutSuccessHandler implements LogoutSuccessHandler, ApplicationContextAware {

    private final Set<Class<? extends LogoutSuccessHandler>> handlers;

    private ApplicationContext appContext;

    @Autowired
    public RootLogoutSuccessHandler(Reflections reflections) {
        handlers = reflections.getSubTypesOf(LogoutSuccessHandler.class);
        handlers.remove(RootLogoutSuccessHandler.class);
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        handlers.stream()
                .map(appContext::getBean)
                .forEach(handler -> {
                    try {
                        handler.onLogoutSuccess(request, response, authentication);
                    } catch (IOException | ServletException e1) { //NOSONAR
                        log.error("Error during logout success", e1);
                        throw new IllegalStateException(e1);
                    }
                });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.appContext = applicationContext;
    }
}
