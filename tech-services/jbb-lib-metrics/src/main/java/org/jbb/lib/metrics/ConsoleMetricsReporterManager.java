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

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;

import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConsoleMetricsReporterManager implements MetricsReporterManager<ConsoleReporter> {

    private final MetricRegistry metricRegistry;

    private ConsoleReporter consoleReporter;

    @PostConstruct
    public void foo() {
        getReporter();
    }

    @Override
    public ConsoleReporter getReporter() {
        if (consoleReporter == null) {
            ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .filter(MetricFilter.contains(""))
                    .build();
            consoleReporter = reporter;
            consoleReporter.start(10, TimeUnit.SECONDS);
        }
        return consoleReporter;
    }

    @Override
    public void configure(MetricProperties properties) {
    }
}
