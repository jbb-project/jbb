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

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;

import java.util.Map;

public interface MetricsGroup {

    MetricType getMetricType();

    void registerMetrics(MetricRegistry metricRegistry);

    default void registerAll(String name, MetricSet metricSet, MetricRegistry registry) {
        for (Map.Entry<String, Metric> entry : metricSet.getMetrics().entrySet()) {
            if (entry.getValue() instanceof MetricSet) {
                registerAll(getMetricType().getCode() + "." + name + "." + entry.getKey(), (MetricSet) entry.getValue(), registry);
            } else {
                registry.register(getMetricType().getCode() + "." + name + "." + entry.getKey(), entry.getValue());
            }
        }
    }
}
