/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.eventbus.metrics;

import org.jbb.lib.metrics.group.MetricsGroup;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventMetricsGroup implements MetricsGroup {
    private final JbbEventMetrics jbbEventMetrics;

    @Override
    public void registerMetrics(CompositeMeterRegistry meterRegistry) {
        jbbEventMetrics.bindTo(meterRegistry);
    }
}
