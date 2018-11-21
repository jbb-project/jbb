/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.metrics;

import org.jbb.system.api.metrics.reporter.ConsoleMetricReporterSettings;
import org.jbb.system.api.metrics.reporter.CsvMetricReporterSettings;
import org.jbb.system.api.metrics.reporter.JmxMetricReporterSettings;
import org.jbb.system.api.metrics.reporter.LogMetricReporterSettings;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MetricSettings {

    @Valid
    @NotNull
    private ConsoleMetricReporterSettings consoleReporterSettings;

    @Valid
    @NotNull
    private JmxMetricReporterSettings jmxReporterSettings;

    @Valid
    @NotNull
    private CsvMetricReporterSettings csvReporterSettings;

    @Valid
    @NotNull
    private LogMetricReporterSettings logReporterSettings;

}
