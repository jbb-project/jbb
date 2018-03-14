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

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class MetricReporterSettings {

    @NotNull
    private Boolean enabled;

    @NotNull
    private Set<MetricType> supportedTypes;

}
