/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.metrics;

import org.jbb.system.api.metrics.MetricSettings;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MetricSettingsTranslator {

    private final ModelMapper modelMapper = new ModelMapper();

    public MetricSettingsDto toDto(MetricSettings metricSettings) {
        return modelMapper.map(metricSettings, MetricSettingsDto.class);
    }

    public MetricSettings toModel(MetricSettingsDto dto) {
        return modelMapper.map(dto, MetricSettings.class);
    }
}
