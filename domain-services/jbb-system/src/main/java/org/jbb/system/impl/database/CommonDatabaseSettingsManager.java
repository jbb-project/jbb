/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database;

import lombok.RequiredArgsConstructor;
import org.jbb.lib.db.DbProperties;
import org.jbb.system.api.database.CommonDatabaseSettings;
import org.jbb.system.api.database.DatabaseSettings;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommonDatabaseSettingsManager {

    private final DbProperties dbProperties;

    public CommonDatabaseSettings getCurrentCommonDatabaseSettings() {
        return CommonDatabaseSettings.builder()
            .minimumIdleConnections(dbProperties.minimumIdle())
            .maximumPoolSize(dbProperties.maxPool())
            .connectionTimeoutMilliseconds(dbProperties.connectionTimeoutMilliseconds())
            .connectionMaxLifetimeMilliseconds(dbProperties.connectionMaxLifetimeMilliseconds())
            .idleTimeoutMilliseconds(dbProperties.idleTimeoutMilliseconds())
            .validationTimeoutMilliseconds(dbProperties.validationTimeoutMilliseconds())
            .leakDetectionThresholdMilliseconds(dbProperties.leakDetectionThresholdMilliseconds())
            .failAtStartingImmediately(dbProperties.failFastDuringInit())
            .dropDatabaseAtStart(dbProperties.dropDbDuringStart())
            .auditEnabled(dbProperties.auditEnabled())
            .build();
    }

    public void setCommonSettings(DatabaseSettings newDatabaseSettings) {
        CommonDatabaseSettings newCommonSettings = newDatabaseSettings.getCommonSettings();
        dbProperties.setProperty(DbProperties.DB_MIN_IDLE_KEY,
            Integer.toString(newCommonSettings.getMinimumIdleConnections()));
        dbProperties.setProperty(DbProperties.DB_MAX_POOL_KEY,
            Integer.toString(newCommonSettings.getMaximumPoolSize()));
        dbProperties.setProperty(DbProperties.DB_CONN_TIMEOUT_MS_KEY,
            Integer.toString(newCommonSettings.getConnectionTimeoutMilliseconds()));
        dbProperties.setProperty(DbProperties.DB_CONN_MAX_LIFETIME_MS_KEY,
            Integer.toString(newCommonSettings.getConnectionMaxLifetimeMilliseconds()));
        dbProperties.setProperty(DbProperties.DB_IDLE_TIMEOUT_MS_KEY,
            Integer.toString(newCommonSettings.getIdleTimeoutMilliseconds()));
        dbProperties.setProperty(DbProperties.DB_VALIDATION_TIMEOUT_MS_KEY,
            Integer.toString(newCommonSettings.getValidationTimeoutMilliseconds()));
        dbProperties.setProperty(DbProperties.DB_LEAK_DETECTION_THRESHOLD_MS_KEY,
            Integer.toString(newCommonSettings.getLeakDetectionThresholdMilliseconds()));
        dbProperties.setProperty(DbProperties.DB_INIT_FAIL_FAST_KEY,
            Boolean.toString(newCommonSettings.isFailAtStartingImmediately()));
        dbProperties.setProperty(DbProperties.DB_DROP_DURING_START_KEY,
            Boolean.toString(newCommonSettings.isDropDatabaseAtStart()));
        dbProperties.setProperty(DbProperties.DB_AUDIT_ENABLED_KEY,
            Boolean.toString(newCommonSettings.isAuditEnabled()));
    }

}
