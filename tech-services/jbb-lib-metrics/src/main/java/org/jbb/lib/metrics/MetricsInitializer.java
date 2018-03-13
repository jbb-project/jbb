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

import java.util.List;

import javax.annotation.PostConstruct;

import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MetricsInitializer {

    private final List<MetricsGroup> metricGroups;
    private final List<ReportersManager> reportersManagers;
    private final CompositeMeterRegistry compositeMeterRegistry;

    private final MetricProperties properties;

    @PostConstruct
    public void register() {
        metricGroups.forEach(registrar -> registrar.registerMetrics(compositeMeterRegistry));
        reportersManagers.forEach(reportersManager -> reportersManager.init(properties));
    }

}
