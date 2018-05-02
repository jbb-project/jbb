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

import org.jbb.lib.metrics.MetricProperties;
import org.jbb.lib.metrics.domain.MeterFilterBuilder;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.jmx.JmxConfig;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JmxReporterManager implements MetricsReporterManager {

    private final CompositeMeterRegistry compositeMeterRegistry;
    private final MeterFilterBuilder meterFilterBuilder;

    private JmxMeterRegistry jmxMeterRegistry;

    @Override
    public void init(MetricProperties properties) {
        JmxMeterRegistry jmxMeterRegistry = new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM);
        this.jmxMeterRegistry = jmxMeterRegistry;

        setUp(properties);
        compositeMeterRegistry.add(jmxMeterRegistry);
    }

    private void setUp(MetricProperties properties) {
        if (properties.jmxReporterEnabled()) {
            jmxMeterRegistry.start();
        } else {
            jmxMeterRegistry.stop();
        }
        jmxMeterRegistry.config().meterFilter(meterFilterBuilder.build(properties.jmxReporterEnabledTypes()));
    }

    @Override
    public void update(MetricProperties properties) {
        setUp(properties);
    }
}
