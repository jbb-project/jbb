/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.database.logic;

import org.jbb.system.api.database.CommonDatabaseSettings;
import org.jbb.system.api.database.DatabaseProviderSettings;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2ManagedServerSettings;
import org.jbb.system.web.database.form.DatabaseSettingsForm;
import org.springframework.stereotype.Component;

@Component
public class FormDatabaseTranslator {

    public DatabaseSettings buildDatabaseSettings(DatabaseSettingsForm form) {
        return DatabaseSettings.builder()
            .commonSettings(buildCommon(form))
            .providerSettings(buildProviderPart(form))
            .build();
    }

    private CommonDatabaseSettings buildCommon(DatabaseSettingsForm form) {
        return CommonDatabaseSettings.builder()
            .minimumIdleConnections(form.getMinimumIdleConnections())
            .maximumPoolSize(form.getMaximumPoolSize())
            .connectionTimeoutMilliseconds(form.getConnectionTimeoutMilliseconds())
            .connectionMaxLifetimeMilliseconds(form.getConnectionMaxLifetimeMilliseconds())
            .idleTimeoutMilliseconds(form.getIdleTimeoutMilliseconds())
            .validationTimeoutMilliseconds(form.getValidationTimeoutMilliseconds())
            .leakDetectionThresholdMilliseconds(form.getLeakDetectionThresholdMilliseconds())
            .failAtStartingImmediately(form.isFailAtStartingImmediately())
            .dropDatabaseAtStart(form.isDropDatabaseAtStart())
            .auditEnabled(form.isAuditEnabled())
            .build();
    }

    private DatabaseProviderSettings buildProviderPart(DatabaseSettingsForm form) {
        return H2ManagedServerSettings.builder()
            .databaseFileName(form.getH2ManagedServerDatabaseFileName())
            .build();
    }


}
