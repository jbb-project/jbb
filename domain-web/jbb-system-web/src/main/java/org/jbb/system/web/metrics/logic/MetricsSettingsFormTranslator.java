/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.metrics.logic;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.EnumUtils;
import org.jbb.system.api.metrics.MetricSettings;
import org.jbb.system.api.metrics.MetricType;
import org.jbb.system.api.metrics.reporter.ConsoleMetricReporterSettings;
import org.jbb.system.api.metrics.reporter.CsvMetricReporterSettings;
import org.jbb.system.api.metrics.reporter.JmxMetricReporterSettings;
import org.jbb.system.api.metrics.reporter.LogMetricReporterSettings;
import org.jbb.system.web.metrics.form.ConsoleReporterSettingsForm;
import org.jbb.system.web.metrics.form.CsvReporterSettingsForm;
import org.jbb.system.web.metrics.form.JmxReporterSettingsForm;
import org.jbb.system.web.metrics.form.LogReporterSettingsForm;
import org.jbb.system.web.metrics.form.MetricsSettingsForm;
import org.springframework.stereotype.Component;

@Component
public class MetricsSettingsFormTranslator {

    public MetricsSettingsForm toForm(MetricSettings settings) {
        ConsoleMetricReporterSettings consoleReporterSettings = settings.getConsoleReporterSettings();
        JmxMetricReporterSettings jmxReporterSettings = settings.getJmxReporterSettings();
        CsvMetricReporterSettings csvReporterSettings = settings.getCsvReporterSettings();
        LogMetricReporterSettings logReporterSettings = settings.getLogReporterSettings();
        return MetricsSettingsForm.builder()
                .consoleReporterSettings(ConsoleReporterSettingsForm.builder()
                        .enabled(consoleReporterSettings.getEnabled())
                        .periodSeconds(consoleReporterSettings.getPeriodSeconds())
                        .metricTypes(mapToTypesForm(consoleReporterSettings.getSupportedTypes()))
                        .build())
                .jmxReporterSettings(JmxReporterSettingsForm.builder()
                        .enabled(jmxReporterSettings.getEnabled())
                        .metricTypes(mapToTypesForm(jmxReporterSettings.getSupportedTypes()))
                        .build())
                .csvReporterSettings(CsvReporterSettingsForm.builder()
                        .enabled(csvReporterSettings.getEnabled())
                        .periodSeconds(csvReporterSettings.getPeriodSeconds())
                        .metricTypes(mapToTypesForm(csvReporterSettings.getSupportedTypes()))
                        .build())
                .logReporterSettings(LogReporterSettingsForm.builder()
                        .enabled(logReporterSettings.getEnabled())
                        .periodSeconds(logReporterSettings.getPeriodSeconds())
                        .metricTypes(mapToTypesForm(logReporterSettings.getSupportedTypes()))
                        .build())
                .build();
    }

    public MetricSettings toModel(MetricsSettingsForm form) {
        ConsoleReporterSettingsForm consoleReporterSettingsForm = form.getConsoleReporterSettings();
        JmxReporterSettingsForm jmxReporterSettingsForm = form.getJmxReporterSettings();
        CsvReporterSettingsForm csvReporterSettingsForm = form.getCsvReporterSettings();
        LogReporterSettingsForm logReporterSettingsForm = form.getLogReporterSettings();
        return MetricSettings.builder()
            .consoleReporterSettings(ConsoleMetricReporterSettings.builder()
                .enabled(consoleReporterSettingsForm.getEnabled())
                .periodSeconds(consoleReporterSettingsForm.getPeriodSeconds())
                .supportedTypes(mapToTypes(consoleReporterSettingsForm.getMetricTypes()))
                .build())
            .jmxReporterSettings(JmxMetricReporterSettings.builder()
                .enabled(jmxReporterSettingsForm.getEnabled())
                .supportedTypes(mapToTypes(jmxReporterSettingsForm.getMetricTypes()))
                .build())
            .csvReporterSettings(CsvMetricReporterSettings.builder()
                .enabled(csvReporterSettingsForm.getEnabled())
                .periodSeconds(csvReporterSettingsForm.getPeriodSeconds())
                .supportedTypes(mapToTypes(csvReporterSettingsForm.getMetricTypes()))
                .build())
            .logReporterSettings(LogMetricReporterSettings.builder()
                .enabled(logReporterSettingsForm.getEnabled())
                .periodSeconds(logReporterSettingsForm.getPeriodSeconds())
                .supportedTypes(mapToTypes(logReporterSettingsForm.getMetricTypes()))
                .build())
            .build();
    }

    private Set<MetricType> mapToTypes(Map<String, Boolean> metricTypes) {
        return metricTypes.entrySet().stream()
            .filter(entry -> entry.getValue() != null && entry.getValue())
            .map(entry -> EnumUtils.getEnum(MetricType.class, entry.getKey()))
            .collect(Collectors.toSet());
    }

    private Map<String, Boolean> mapToTypesForm(Set<MetricType> supportedTypes) {
        Map<String, Boolean> result = Maps.newHashMap();
        Arrays.stream(MetricType.values()).forEach(metricType ->
            result.put(metricType.name(), supportedTypes.contains(metricType))
        );
        return result;
    }

}