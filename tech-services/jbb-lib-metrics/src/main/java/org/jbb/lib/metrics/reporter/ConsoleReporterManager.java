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

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

import org.jbb.lib.metrics.MetricProperties;
import org.jbb.lib.metrics.domain.MeterFilterBuilder;
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
public class ConsoleReporterManager implements MetricsReporterManager {

    private final CompositeMeterRegistry compositeMeterRegistry;
    private final MeterFilterBuilder meterFilterBuilder;

    private ConsoleReporter reporter;
    private DropwizardMeterRegistry dropwizardMeterRegistry;

    @Override
    public void init(MetricProperties properties) {
        setUpConsoleReporter(properties);
    }

    @Override
    public void update(MetricProperties properties) {
        reporter.stop();
        compositeMeterRegistry.remove(dropwizardMeterRegistry);
        setUpConsoleReporter(properties);
    }

    private void setUpConsoleReporter(MetricProperties properties) {
        MetricRegistry dropwizardRegistry = new MetricRegistry();

        ConsoleReporter newReporter = ConsoleReporter.forRegistry(dropwizardRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();

        if (properties.consoleReporterEnabled()) {
            newReporter.start(properties.consoleReporterPeriodSeconds(), TimeUnit.SECONDS);
        } else {
            newReporter.stop();
        }
        this.reporter = newReporter;

        DropwizardConfig consoleConfig = new DropwizardConfig() {
            @Override
            public String prefix() {
                return "console";
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

        dropwizardMeterRegistry.config()
                .meterFilter(meterFilterBuilder.build(properties.consoleReporterEnabledTypes()));

        this.dropwizardMeterRegistry = dropwizardMeterRegistry;
        compositeMeterRegistry.add(dropwizardMeterRegistry);
    }
}
