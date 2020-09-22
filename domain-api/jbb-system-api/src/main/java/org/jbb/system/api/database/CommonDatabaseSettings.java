/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.database;

import javax.validation.constraints.Min;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonDatabaseSettings {

    @Min(1)
    private int minimumIdleConnections;

    @Min(1)
    private int maximumPoolSize;

    @Min(0)
    private int connectionTimeoutMilliseconds;

    @Min(0)
    private int connectionMaxLifetimeMilliseconds;

    @Min(0)
    private int idleTimeoutMilliseconds;

    @Min(0)
    private int validationTimeoutMilliseconds;

    @Min(0)
    private int leakDetectionThresholdMilliseconds;

    private boolean failAtStartingImmediately;

    private boolean statisticsEnabled;

    private boolean auditEnabled;

}
