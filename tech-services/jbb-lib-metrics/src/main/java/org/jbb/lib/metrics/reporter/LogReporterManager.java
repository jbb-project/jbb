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

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;

import org.jbb.lib.metrics.MetricProperties;
import org.jbb.lib.metrics.domain.MeterFilterBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.dropwizard.DropwizardConfig;
import io.micrometer.core.instrument.dropwizard.DropwizardMeterRegistry;
import io.micrometer.core.instrument.util.HierarchicalNameMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LogReporterManager implements MetricsReporterManager {

    private final CompositeMeterRegistry compositeMeterRegistry;
    private final MeterFilterBuilder meterFilterBuilder;

    @Override
    public void init(MetricProperties properties) {
        MetricRegistry dropwizardRegistry = new MetricRegistry();

        Slf4jReporter reporter = Slf4jReporter.forRegistry(dropwizardRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .outputTo(LoggerFactory.getLogger("metrics"))
                .build();
        if (properties.logReporterEnabled()) {
            reporter.start(properties.logReporterPeriodSeconds(), TimeUnit.SECONDS);
        } else {
            reporter.stop();
        }

        DropwizardConfig logConfig = new DropwizardConfig() {
            @Override
            public String prefix() {
                return "slf4j";
            }

            @Override
            public String get(String key) {
                return null;
            }
        };

        DropwizardMeterRegistry dropwizardMeterRegistry = new DropwizardMeterRegistry(logConfig, dropwizardRegistry, HierarchicalNameMapper.DEFAULT, Clock.SYSTEM) {

            @Override
            protected Double nullGaugeValue() {
                return Double.NaN;
            }
        };

        dropwizardMeterRegistry.config().meterFilter(meterFilterBuilder.build(properties.logReporterEnabledTypes()));
        compositeMeterRegistry.add(dropwizardMeterRegistry);

    }

}
