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


import org.jbb.system.api.metrics.MetricSettings;
import org.jbb.system.api.metrics.MetricsConfigException;
import org.jbb.system.impl.BaseIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultMetricSettingsServiceIT extends BaseIT {

    @Autowired
    private DefaultMetricSettingsService defaultMetricSettingsService;

    @Test
    public void shouldGetMetricsSettings() {
        // when
        MetricSettings metricSettings = defaultMetricSettingsService.getMetricSettings();

        // then
        assertThat(metricSettings).isNotNull();
    }

    @Test
    public void shouldSetNewMetricSettings() {
        // given
        MetricSettings metricSettings = defaultMetricSettingsService.getMetricSettings();
        metricSettings.getJmxReporterSettings().setEnabled(true);

        // when
        defaultMetricSettingsService.setMetricSettings(metricSettings);
        MetricSettings newMetricSettings = defaultMetricSettingsService.getMetricSettings();

        // then
        assertThat(newMetricSettings.getJmxReporterSettings().getEnabled()).isTrue();
    }

    @Test(expected = MetricsConfigException.class)
    public void shouldThrowMetricsConfigException_whenMetricSettingsAreNotValid() {
        // given
        MetricSettings metricSettings = defaultMetricSettingsService.getMetricSettings();
        metricSettings.getJmxReporterSettings().setEnabled(null);

        // when
        defaultMetricSettingsService.setMetricSettings(metricSettings);

        // then
        // throw MetricsConfigException
    }
}