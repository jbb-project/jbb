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

import com.google.common.collect.Maps;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConsoleReportersManager extends ReportersManager {

    private final MetricRegistry metricRegistry;

    private Map<MetricType, ConsoleReporter> consoleReporters = Maps.newEnumMap(MetricType.class);

    @Override
    void init(MetricProperties properties, MetricType type) {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.startsWith(type.getCode()))
                .build();
        reporter.start(10, TimeUnit.SECONDS);
        consoleReporters.put(type, reporter);
    }

    @Override
    void configure(MetricProperties properties, MetricType type) {
        consoleReporters.get(type).stop();
        init(properties, type);
    }

}
