/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.metrics;

import org.jbb.lib.properties.ModulePropertiesFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.spring.web.servlet.DefaultWebMvcTagsProvider;
import io.micrometer.spring.web.servlet.WebMvcMetricsFilter;
import io.micrometer.spring.web.servlet.WebMvcTagsProvider;

@Configuration
@ComponentScan
public class MetricsConfig {

    @Bean
    public CompositeMeterRegistry meterRegistry() {
        return new CompositeMeterRegistry();
    }

    @Bean
    public MetricProperties metricProperties(ModulePropertiesFactory propertiesFactory) {
        return propertiesFactory.create(MetricProperties.class);
    }

    @Bean
    public DefaultWebMvcTagsProvider servletTagsProvider() {
        return new DefaultWebMvcTagsProvider();
    }

    @Bean
    public WebMvcMetricsFilter webMetricsFilter(MeterRegistry registry,
                                                WebMvcTagsProvider tagsProvider,
                                                WebApplicationContext ctx) {
        return new WebMvcMetricsFilter(registry, tagsProvider,
                "request",
                true,
                new HandlerMappingIntrospector(ctx));
    }

}
