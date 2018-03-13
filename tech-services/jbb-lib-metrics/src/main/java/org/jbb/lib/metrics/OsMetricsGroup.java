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

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;

@Component
public class OsMetricsGroup implements MetricsGroup {
    @Override
    public void registerMetrics(CompositeMeterRegistry meterRegistry) {
        new FileDescriptorMetrics().bindTo(meterRegistry);
    }
}
