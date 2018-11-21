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

import org.jbb.lib.metrics.MetricProperties;
import org.jbb.lib.metrics.MetricPropertyChangeListener;
import org.jbb.lib.metrics.MetricsRegistrar;
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

import static org.jbb.lib.metrics.MetricProperties.METRICS_REPORTER_CONSOLE_ENABLED;
import static org.jbb.lib.metrics.MetricProperties.METRICS_REPORTER_CONSOLE_ENABLED_TYPES;
import static org.jbb.lib.metrics.MetricProperties.METRICS_REPORTER_CONSOLE_PERIOD_SECONDS;
import static org.jbb.lib.metrics.MetricProperties.METRICS_REPORTER_CSV_ENABLED;
import static org.jbb.lib.metrics.MetricProperties.METRICS_REPORTER_CSV_ENABLED_TYPES;
import static org.jbb.lib.metrics.MetricProperties.METRICS_REPORTER_CSV_PERIOD_SECONDS;
import static org.jbb.lib.metrics.MetricProperties.METRICS_REPORTER_JMX_ENABLED;
import static org.jbb.lib.metrics.MetricProperties.METRICS_REPORTER_JMX_ENABLED_TYPES;
import static org.jbb.lib.metrics.MetricProperties.METRICS_REPORTER_LOG_ENABLED;
import static org.jbb.lib.metrics.MetricProperties.METRICS_REPORTER_LOG_ENABLED_TYPES;
import static org.jbb.lib.metrics.MetricProperties.METRICS_REPORTER_LOG_PERIOD_SECONDS;

@Component
@RequiredArgsConstructor
public class MetricSettingsSaver {

    private final MetricProperties metricProperties;
    private final MetricPropertyChangeListener metricPropertyChangeListener;
    private final MetricsRegistrar metricsRegistrar;

    public void save(MetricSettings newMetricSettings) {
        metricProperties.removePropertyChangeListener(metricPropertyChangeListener);
        try {
            updateProperties(newMetricSettings);
        } finally {
            metricProperties.addPropertyChangeListener(metricPropertyChangeListener);
            metricsRegistrar.update();
        }
    }

    private void updateProperties(MetricSettings newMetricSettings) {
        updateProperties(newMetricSettings.getConsoleReporterSettings());
        updateProperties(newMetricSettings.getJmxReporterSettings());
        updateProperties(newMetricSettings.getCsvReporterSettings());
        updateProperties(newMetricSettings.getLogReporterSettings());
    }

    private void updateProperties(ConsoleMetricReporterSettings consoleSettings) {
        metricProperties.setProperty(METRICS_REPORTER_CONSOLE_ENABLED, Boolean.toString(consoleSettings.getEnabled()));
        metricProperties.setProperty(METRICS_REPORTER_CONSOLE_ENABLED_TYPES, join(consoleSettings.getSupportedTypes()));
        metricProperties.setProperty(METRICS_REPORTER_CONSOLE_PERIOD_SECONDS, Integer.toString(consoleSettings.getPeriodSeconds()));
    }

    private void updateProperties(JmxMetricReporterSettings jmxSettings) {
        metricProperties.setProperty(METRICS_REPORTER_JMX_ENABLED, Boolean.toString(jmxSettings.getEnabled()));
        metricProperties.setProperty(METRICS_REPORTER_JMX_ENABLED_TYPES, join(jmxSettings.getSupportedTypes()));
    }

    private void updateProperties(CsvMetricReporterSettings csvSettings) {
        metricProperties.setProperty(METRICS_REPORTER_CSV_ENABLED, Boolean.toString(csvSettings.getEnabled()));
        metricProperties.setProperty(METRICS_REPORTER_CSV_ENABLED_TYPES, join(csvSettings.getSupportedTypes()));
        metricProperties.setProperty(METRICS_REPORTER_CSV_PERIOD_SECONDS, Integer.toString(csvSettings.getPeriodSeconds()));
    }

    private void updateProperties(LogMetricReporterSettings logSettings) {
        metricProperties.setProperty(METRICS_REPORTER_LOG_ENABLED, Boolean.toString(logSettings.getEnabled()));
        metricProperties.setProperty(METRICS_REPORTER_LOG_ENABLED_TYPES, join(logSettings.getSupportedTypes()));
        metricProperties.setProperty(METRICS_REPORTER_LOG_PERIOD_SECONDS, Integer.toString(logSettings.getPeriodSeconds()));
    }

    private String join(Set<MetricType> supportedTypes) {
        return supportedTypes.stream()
                .map(Enum::toString)
                .collect(Collectors.joining(","));
    }

}
