/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp;

import org.jbb.lib.properties.ModuleConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan("org.jbb.webapp")
public class MvcConfig extends WebMvcConfigurationSupport {

    private static final String VIEW_PREFIX = "/WEB-INF/";
    private static final String VIEW_SUFFIX = ".jsp";

    @Bean
    public InternalResourceViewResolver viewResolver() {
        return new InternalResourceViewResolver(VIEW_PREFIX, VIEW_SUFFIX);
    }

    @Bean
    public BasicProperties basicProperties() {
        return ModuleConfigFactory.create(BasicProperties.class);
    }
}

