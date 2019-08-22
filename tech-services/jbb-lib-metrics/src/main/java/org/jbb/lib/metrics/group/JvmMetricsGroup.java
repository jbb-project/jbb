/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.metrics.group;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;

@Component
public class JvmMetricsGroup implements MetricsGroup, AutoCloseable {

    private final JvmGcMetrics jvmGcMetrics = new JvmGcMetrics();

    @Override
    public void registerMetrics(CompositeMeterRegistry meterRegistry) {
        new ClassLoaderMetrics().bindTo(meterRegistry);
        new JvmMemoryMetrics().bindTo(meterRegistry);
        jvmGcMetrics.bindTo(meterRegistry);
        new JvmThreadMetrics().bindTo(meterRegistry);
    }

    @Override
    public void close() throws Exception {
        jvmGcMetrics.close();
    }
}
