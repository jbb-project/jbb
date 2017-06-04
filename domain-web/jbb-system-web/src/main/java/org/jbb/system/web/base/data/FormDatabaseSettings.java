/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.base.data;

import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.web.base.form.DatabaseSettingsForm;

public class FormDatabaseSettings implements DatabaseSettings {
    private DatabaseSettingsForm form;

    public FormDatabaseSettings(DatabaseSettingsForm form) {
        this.form = form;
    }

    @Override
    public String databaseFileName() {
        return form.getDatabaseFileName();
    }

    @Override
    public int minimumIdleConnections() {
        return form.getMinimumIdleConnections();
    }

    @Override
    public int maximumPoolSize() {
        return form.getMaximumPoolSize();
    }

    @Override
    public int connectionTimeoutMilliseconds() {
        return form.getConnectionTimeOutMilliseconds();
    }

    @Override
    public boolean failAtStartingImmediately() {
        return form.isFailAtStartingImmediately();
    }

    @Override
    public boolean dropDatabaseAtStart() {
        return form.isDropDatabaseAtStart();
    }

    @Override
    public boolean auditEnabled() {
        return form.isAuditEnabled();
    }
}
