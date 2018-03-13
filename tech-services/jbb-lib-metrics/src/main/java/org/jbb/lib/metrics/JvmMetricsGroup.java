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

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.ClassLoadingGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.JvmAttributeGaugeSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;

@Component
public class JvmMetricsGroup implements MetricsGroup {

    @Override
    public MetricType getMetricType() {
        return MetricType.JVM;
    }

    @Override
    public void registerMetrics(MetricRegistry metricRegistry) {
        registerAll("gc", new GarbageCollectorMetricSet(), metricRegistry);
        registerAll("buffers", new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()), metricRegistry);
        registerAll("memory", new MemoryUsageGaugeSet(), metricRegistry);
        registerAll("threads", new ThreadStatesGaugeSet(), metricRegistry);
        registerAll("classLoading", new ClassLoadingGaugeSet(), metricRegistry);
        registerAll("attributes", new JvmAttributeGaugeSet(), metricRegistry);
    }
}
