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

import org.jbb.lib.metrics.group.MetricsGroup;
import org.jbb.lib.metrics.reporter.MetricsReporterManager;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.PostConstruct;

import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MetricsRegistrar {

    private final List<MetricsGroup> metricGroups;
    private final List<MetricsReporterManager> metricsReporterManagers;

    private final CompositeMeterRegistry compositeMeterRegistry;

    private final MetricProperties properties;

    @PostConstruct
    public void register() {
        metricGroups.forEach(registrar -> registrar.registerMetrics(compositeMeterRegistry));
        metricsReporterManagers.forEach(reporterManager -> reporterManager.init(properties));
    }

    public void update() {
        metricsReporterManagers.forEach(reporterManager -> reporterManager.update(properties));
    }

}
