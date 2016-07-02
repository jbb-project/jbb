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


import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.Set;

public class InterceptorRegistryInserter {
    @Autowired
    private ApplicationContext applicationContext;

    private Set<Class<? extends HandlerInterceptorAdapter>> interceptors;

    public InterceptorRegistryInserter() {
        Reflections reflections = new Reflections("org.jbb");
        interceptors = reflections.getSubTypesOf(HandlerInterceptorAdapter.class);
    }

    public void fill(InterceptorRegistry registry) {
        interceptors.forEach(interceptorClass -> {
            registry.addInterceptor(applicationContext.getBean(interceptorClass));
        });
    }
}
