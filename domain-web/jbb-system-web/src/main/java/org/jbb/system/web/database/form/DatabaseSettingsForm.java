/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.database.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseSettingsForm {

    private int minimumIdleConnections;

    private int maximumPoolSize;

    private int connectionTimeoutMilliseconds;

    private int connectionMaxLifetimeMilliseconds;

    private int idleTimeoutMilliseconds;

    private int validationTimeoutMilliseconds;

    private int leakDetectionThresholdMilliseconds;

    private boolean failAtStartingImmediately;

    private boolean dropDatabaseAtStart;

    private boolean auditEnabled;

    private String currentDatabaseProviderName;

    private H2InMemoryForm h2inMemorySettings = new H2InMemoryForm();

    private H2EmbeddedForm h2embeddedSettings = new H2EmbeddedForm();

    private H2ManagedServerForm h2managedServerSettings = new H2ManagedServerForm();

    private H2RemoteServerForm h2remoteServerSettings = new H2RemoteServerForm();

}
