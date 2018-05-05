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

import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.properties.ModulePropertiesFactory;
import org.jbb.lib.properties.PropertiesConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@Import({CommonsConfig.class, PropertiesConfig.class})
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
    public MetricPropertyChangeListener metricPropertyChangeListener(MetricsRegistrar metricsRegistrar, MetricProperties metricProperties) {
        MetricPropertyChangeListener listener = new MetricPropertyChangeListener(metricsRegistrar);
        metricProperties.addPropertyChangeListener(listener);
        return listener;
    }

}
