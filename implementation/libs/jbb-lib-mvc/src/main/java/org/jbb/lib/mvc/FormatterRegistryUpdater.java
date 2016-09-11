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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;

import java.util.Set;

public class FormatterRegistryUpdater implements ApplicationContextAware {
    private final Set<Class<? extends Formatter>> formatters;

    private ApplicationContext appContext;

    @Autowired
    public FormatterRegistryUpdater(Reflections reflections) {
        formatters = reflections.getSubTypesOf(Formatter.class);
    }

    public void fill(FormatterRegistry registry) {
        formatters.forEach(formatterClass ->
                        registry.addFormatter(appContext.getBean(formatterClass))
        );
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }
}
