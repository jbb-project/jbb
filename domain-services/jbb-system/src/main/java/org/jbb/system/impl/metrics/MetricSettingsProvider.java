/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.metrics;

import org.apache.commons.lang3.EnumUtils;
import org.jbb.lib.metrics.MetricProperties;
import org.jbb.lib.metrics.domain.MetricPrefix;
import org.jbb.system.api.metrics.MetricSettings;
import org.jbb.system.api.metrics.MetricType;
import org.jbb.system.api.metrics.reporter.ConsoleMetricReporterSettings;
import org.jbb.system.api.metrics.reporter.CsvMetricReporterSettings;
import org.jbb.system.api.metrics.reporter.JmxMetricReporterSettings;
import org.jbb.system.api.metrics.reporter.LogMetricReporterSettings;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MetricSettingsProvider {

    private final MetricProperties metricProperties;

    public MetricSettings currentMetricSettings() {
        return MetricSettings.builder()
                .consoleReporterSettings(ConsoleMetricReporterSettings.builder()
                        .enabled(metricProperties.consoleReporterEnabled())
                        .supportedTypes(mapToTypes(metricProperties.consoleReporterEnabledTypes()))
                        .periodSeconds(metricProperties.consoleReporterPeriodSeconds())
                        .build())
                .jmxReporterSettings(JmxMetricReporterSettings.builder()
                        .enabled(metricProperties.jmxReporterEnabled())
                        .supportedTypes(mapToTypes(metricProperties.jmxReporterEnabledTypes()))
                        .build())
                .csvReporterSettings(CsvMetricReporterSettings.builder()
                        .enabled(metricProperties.csvReporterEnabled())
                        .supportedTypes(mapToTypes(metricProperties.csvReporterEnabledTypes()))
                        .periodSeconds(metricProperties.csvReporterPeriodSeconds())
                        .build())
                .logReporterSettings(LogMetricReporterSettings.builder()
                        .enabled(metricProperties.logReporterEnabled())
                        .supportedTypes(mapToTypes(metricProperties.logReporterEnabledTypes()))
                        .periodSeconds(metricProperties.logReporterPeriodSeconds())
                        .build())
                .build();
    }

    private Set<MetricType> mapToTypes(Set<MetricPrefix> prefixes) {
        return prefixes.stream()
                .map(this::mapToType)
                .collect(Collectors.toSet());
    }

    private MetricType mapToType(MetricPrefix prefix) {
        return EnumUtils.getEnum(MetricType.class, prefix.name());
    }
}
