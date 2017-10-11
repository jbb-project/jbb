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

import java.util.List;
import org.jbb.lib.commons.JbbBeanSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Component;

@Component
public class FormatterRegistryUpdater {

    private final List<? extends Formatter> formatters;

    @Autowired
    public FormatterRegistryUpdater(JbbBeanSearch jbbBeanSearch) {
        formatters = jbbBeanSearch.getBeanClasses(Formatter.class);
    }

    public void fill(FormatterRegistry registry) {
        formatters.forEach(registry::addFormatter);
    }

}
