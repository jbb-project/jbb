/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.metrics.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetricsSettingsForm {

    private ConsoleReporterSettingsForm consoleReporterSettings;

    private JmxReporterSettingsForm jmxReporterSettings;

    private CsvReporterSettingsForm csvReporterSettings;

    private LogReporterSettingsForm logReporterSettings;

}
