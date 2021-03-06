/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.metrics.reporter;

import org.jbb.system.api.metrics.MetricType;

import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsoleMetricReporterSettings extends MetricReporterSettings {

    @NotNull
    @Min(1)
    private Integer periodSeconds;

    @Builder
    ConsoleMetricReporterSettings(Boolean enabled, Set<MetricType> supportedTypes, Integer periodSeconds) {
        super(enabled, supportedTypes);
        this.periodSeconds = periodSeconds;
    }
}
