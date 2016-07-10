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
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;

import java.util.Set;

public class FormatterRegistryInserter {
    private static final String ROOT_JBB_PACKAGE = "org.jbb";
    private final Set<Class<? extends Formatter>> formaters;
    @Autowired
    private ApplicationContext appContext;

    public FormatterRegistryInserter() {
        Reflections reflections = new Reflections(ROOT_JBB_PACKAGE);
        formaters = reflections.getSubTypesOf(Formatter.class);
    }

    public void fill(FormatterRegistry registry) {
        formaters.forEach(formatterClass ->
                        registry.addFormatter(appContext.getBean(formatterClass))
        );
    }
}
