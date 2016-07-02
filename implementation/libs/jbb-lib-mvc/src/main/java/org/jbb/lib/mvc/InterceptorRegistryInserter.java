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
    private static final String ROOT_JBB_PACKAGE = "org.jbb";

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private ApplicationContext appContext;
    private final Set<Class<? extends HandlerInterceptorAdapter>> interceptors;

    public InterceptorRegistryInserter() {
        Reflections reflections = new Reflections(ROOT_JBB_PACKAGE);
        interceptors = reflections.getSubTypesOf(HandlerInterceptorAdapter.class);
    }

    public void fill(InterceptorRegistry registry) {
        interceptors.forEach(interceptorClass ->
                        registry.addInterceptor(appContext.getBean(interceptorClass))
        );
    }
}
