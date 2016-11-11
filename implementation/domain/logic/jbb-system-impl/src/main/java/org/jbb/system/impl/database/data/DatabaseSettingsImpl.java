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
import org.jbb.system.api.model.DatabaseSettings;

import javax.validation.constraints.Min;

import lombok.Setter;

@Setter
public class DatabaseSettingsImpl implements DatabaseSettings {
    @NotEmpty
    private String databaseFileName;

    @Min(1)
    private int minimumIdleConnections;

    @Min(1)
    private int maximumPoolSize;

    @Min(0)
    private int connectionTImeOutMilliseconds;

    private boolean failAtStartingImmediately;

    private boolean dropDatabaseAtStart;

    @Override
    public String databaseFileName() {
        return databaseFileName;
    }

    @Override
    public int minimumIdleConnections() {
        return minimumIdleConnections;
    }

    @Override
    public int maximumPoolSize() {
        return maximumPoolSize;
    }

    @Override
    public int connectionTimeoutMilliseconds() {
        return connectionTImeOutMilliseconds;
    }

    @Override
    public boolean failAtStartingImmediately() {
        return failAtStartingImmediately;
    }

    @Override
    public boolean dropDatabaseAtStart() {
        return dropDatabaseAtStart;
    }
}
