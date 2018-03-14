/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.logging.metrics;

import org.jbb.lib.metrics.group.MetricsGroup;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.binder.logging.LogbackMetrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoggingMetricsGroup implements MetricsGroup {
    private final LogbackMetrics logbackMetrics;

    @Override
    public void registerMetrics(CompositeMeterRegistry meterRegistry) {
        logbackMetrics.bindTo(meterRegistry);
    }
}
