/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.database;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@ApiModel("CommonDatabaseSettings")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonDatabaseSettingsDto {

    private Integer minimumIdleConnections;

    private Integer maximumPoolSize;

    private Integer connectionTimeoutMilliseconds;

    private Integer connectionMaxLifetimeMilliseconds;

    private Integer idleTimeoutMilliseconds;

    private Integer validationTimeoutMilliseconds;

    private Integer leakDetectionThresholdMilliseconds;

    private Boolean failAtStartingImmediately;

    private Boolean statisticsEnabled;

    private Boolean auditEnabled;
}
