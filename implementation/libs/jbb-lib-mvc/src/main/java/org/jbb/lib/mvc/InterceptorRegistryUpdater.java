/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc;


import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.Arrays;

@Component
public class InterceptorRegistryUpdater implements ApplicationContextAware {

    private ApplicationContext appContext;

    public void fill(InterceptorRegistry registry) {
        String[] interceptorNames = appContext.getBeanNamesForType(HandlerInterceptorAdapter.class);
        Arrays.stream(interceptorNames).forEach(interceptorName ->
                registry.addInterceptor((HandlerInterceptor) appContext.getBean(interceptorName))
        );
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.appContext = applicationContext;
    }
}
