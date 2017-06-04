/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.data;

import org.hibernate.validator.constraints.NotEmpty;
import org.jbb.system.api.database.DatabaseSettings;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseSettingsImpl implements DatabaseSettings {
    @NotEmpty
    private String databaseFileName;

    @Min(1)
    private int minimumIdleConnections;

    @Min(1)
    private int maximumPoolSize;

    @Min(0)
    private int connectionTimeoutMilliseconds;

    private boolean failAtStartingImmediately;

    private boolean dropDatabaseAtStart;

    private boolean auditEnabled;

}
