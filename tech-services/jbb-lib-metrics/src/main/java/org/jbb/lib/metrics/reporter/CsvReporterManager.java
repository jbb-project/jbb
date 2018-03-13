/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.metrics.reporter;

import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.MetricRegistry;

import org.jbb.lib.commons.JbbMetaData;
import org.jbb.lib.metrics.MetricProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.dropwizard.DropwizardConfig;
import io.micrometer.core.instrument.dropwizard.DropwizardMeterRegistry;
import io.micrometer.core.instrument.util.HierarchicalNameMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CsvReporterManager implements ReporterManager {
    private final JbbMetaData jbbMetaData;
    private final CompositeMeterRegistry compositeMeterRegistry;

    @PostConstruct
    public void createDirectoryIfNeeded() {
        String metricsDirPath = jbbMetaData.jbbMetricsDirectory();

        File metricsDir = new File(metricsDirPath);
        if (!metricsDir.exists()) {
            metricsDir.mkdir();
        }
    }

    @Override
    public void init(MetricProperties properties) {
        MetricRegistry dropwizardRegistry = new MetricRegistry();

        CsvReporter reporter = CsvReporter.forRegistry(dropwizardRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .withCsvFileProvider(new SafeNameCsvFileProvider())
                .build(new File(jbbMetaData.jbbMetricsDirectory()));

        reporter.start(10, TimeUnit.SECONDS);

        DropwizardConfig consoleConfig = new DropwizardConfig() {
            @Override
            public String prefix() {
                return "csv";
            }

            @Override
            public String get(String key) {
                return null;
            }
        };

        DropwizardMeterRegistry dropwizardMeterRegistry = new DropwizardMeterRegistry(consoleConfig, dropwizardRegistry, HierarchicalNameMapper.DEFAULT, Clock.SYSTEM) {

            @Override
            protected Double nullGaugeValue() {
                return Double.NaN;
            }
        };

        compositeMeterRegistry.add(dropwizardMeterRegistry);
    }
}
