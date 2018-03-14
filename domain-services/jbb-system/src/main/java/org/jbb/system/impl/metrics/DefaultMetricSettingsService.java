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

import org.apache.commons.lang3.Validate;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.system.api.metrics.MetricSettings;
import org.jbb.system.api.metrics.MetricSettingsService;
import org.jbb.system.api.metrics.MetricsConfigException;
import org.jbb.system.event.MetricSettingsChangedEvent;
import org.springframework.stereotype.Service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultMetricSettingsService implements MetricSettingsService {

    private final MetricSettingsProvider settingsProvider;
    private final MetricSettingsSaver settingsSaver;
    private final Validator validator;
    private final JbbEventBus eventBus;

    @Override
    public MetricSettings getMetricSettings() {
        return settingsProvider.currentMetricSettings();
    }

    @Override
    public void setMetricSettings(MetricSettings newMetricSettings) {
        Validate.notNull(newMetricSettings);

        Set<ConstraintViolation<MetricSettings>> validationResult = validator.validate(newMetricSettings);
        if (!validationResult.isEmpty()) {
            throw new MetricsConfigException(validationResult);
        }

        settingsSaver.save(newMetricSettings);
        eventBus.post(new MetricSettingsChangedEvent());
    }
}
