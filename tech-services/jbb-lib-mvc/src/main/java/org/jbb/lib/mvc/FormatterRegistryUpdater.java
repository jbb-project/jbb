/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc;

import org.jbb.lib.commons.JbbBeanSearch;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FormatterRegistryUpdater {

    private final List<? extends Formatter> formatters;

    public FormatterRegistryUpdater(JbbBeanSearch jbbBeanSearch) {
        formatters = jbbBeanSearch.getBeanClasses(Formatter.class);
    }

    public void fill(FormatterRegistry registry) {
        formatters.forEach(registry::addFormatter);
    }

}
