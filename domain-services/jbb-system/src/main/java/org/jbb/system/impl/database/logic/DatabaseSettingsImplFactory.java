/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.logic;

import org.jbb.lib.db.DbProperties;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.impl.database.data.DatabaseSettingsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSettingsImplFactory {
    private final DbProperties dbProperties;

    @Autowired
    public DatabaseSettingsImplFactory(DbProperties dbProperties) {
        this.dbProperties = dbProperties;
    }

    public DatabaseSettings currentDatabaseSettings() {
        DatabaseSettingsImpl currentDbSettings = new DatabaseSettingsImpl();
        currentDbSettings.setDatabaseFileName(dbProperties.dbFilename());
        currentDbSettings.setMinimumIdleConnections(dbProperties.minimumIdle());
        currentDbSettings.setMaximumPoolSize(dbProperties.maxPool());
        currentDbSettings.setConnectionTimeoutMilliseconds(dbProperties.connectionTimeoutMiliseconds());
        currentDbSettings.setFailAtStartingImmediately(dbProperties.failFastDuringInit());
        currentDbSettings.setDropDatabaseAtStart(dbProperties.dropDbDuringStart());
        currentDbSettings.setAuditEnabled(dbProperties.auditEnabled());
        return currentDbSettings;
    }

    public DatabaseSettings create(DatabaseSettings databaseSettings) {
        DatabaseSettingsImpl result = new DatabaseSettingsImpl();
        result.setDatabaseFileName(databaseSettings.getDatabaseFileName());
        result.setMinimumIdleConnections(databaseSettings.getMinimumIdleConnections());
        result.setMaximumPoolSize(databaseSettings.getMaximumPoolSize());
        result.setConnectionTimeoutMilliseconds(databaseSettings.getConnectionTimeoutMilliseconds());
        result.setFailAtStartingImmediately(databaseSettings.isFailAtStartingImmediately());
        result.setDropDatabaseAtStart(databaseSettings.isDropDatabaseAtStart());
        result.setAuditEnabled(databaseSettings.isAuditEnabled());
        return result;
    }
}
