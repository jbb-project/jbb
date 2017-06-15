/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.database;

public interface DatabaseSettings {
    String getDatabaseFileName();

    int getMinimumIdleConnections();

    int getMaximumPoolSize();

    int getConnectionTimeoutMilliseconds();

    int getConnectionMaxLifetimeMilliseconds();

    int getIdleTimeoutMilliseconds();

    int getValidationTimeoutMilliseconds();

    int getLeakDetectionThreshold();

    boolean isFailAtStartingImmediately();

    boolean isDropDatabaseAtStart();

    boolean isAuditEnabled();
}
