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
import com.codahale.metrics.jmx.JmxReporter;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.dropwizard.DropwizardMeterRegistry;
import io.micrometer.core.instrument.util.HierarchicalNameMapper;
import io.micrometer.jmx.JmxConfig;

public class JmxMeterRegistry extends DropwizardMeterRegistry {
    private final JmxReporter reporter;

    public JmxMeterRegistry(JmxConfig config, Clock clock) {
        this(config, clock, HierarchicalNameMapper.DEFAULT);
    }

    public JmxMeterRegistry(JmxConfig config, Clock clock, HierarchicalNameMapper nameMapper) {
        this(config, clock, nameMapper, new MetricRegistry());
    }

    public JmxMeterRegistry(JmxConfig config, Clock clock, HierarchicalNameMapper nameMapper, MetricRegistry metricRegistry) {
        this(config, clock, nameMapper, metricRegistry, defaultJmxReporter(config, metricRegistry));
    }

    public JmxMeterRegistry(JmxConfig config, Clock clock, HierarchicalNameMapper nameMapper, MetricRegistry metricRegistry, JmxReporter jmxReporter) {
        super(config, metricRegistry, nameMapper, clock);
        this.reporter = jmxReporter;
        this.reporter.start();
    }

    private static JmxReporter defaultJmxReporter(JmxConfig config, MetricRegistry metricRegistry) {
        return JmxReporter.forRegistry(metricRegistry).inDomain(config.domain()).build();
    }

    public void stop() {
        this.reporter.stop();
    }

    public void start() {
        this.reporter.start();
    }

    public void close() {
        this.stop();
        super.close();
    }

    protected Double nullGaugeValue() {
        return Double.NaN;
    }
}