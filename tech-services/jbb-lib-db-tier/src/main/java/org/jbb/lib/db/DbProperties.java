/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;
import org.jbb.lib.properties.ModuleProperties;

@Config.HotReload(type = Config.HotReloadType.ASYNC)
@Sources({"file:${jbb.home}/jbb-lib-db-tier.properties"})
public interface DbProperties extends ModuleProperties { // NOSONAR (key names should stay)

    String DB_MIN_IDLE_KEY = "database.minimum.idle";
    String DB_MAX_POOL_KEY = "database.maximum.pool";
    String DB_CONN_TIMEOUT_MS_KEY = "database.connection.timeoutMilli";
    String DB_CONN_MAX_LIFETIME_MS_KEY = "database.connection.maxLifetimeMilli";
    String DB_IDLE_TIMEOUT_MS_KEY = "database.idle.timeoutMilli";
    String DB_LEAK_DETECTION_THRESHOLD_KEY = "database.leakDetection.threshold";
    String DB_VALIDATION_TIMEOUT_MS_KEY = "database.validation.timeoutMilli";
    String DB_INIT_FAIL_FAST_KEY = "database.init.fail.fast";
    String DB_DROP_DURING_START_KEY = "database.drop.during.start";
    String DB_AUDIT_ENABLED_KEY = "database.audit.enabled";

    String H2_MANAGED_SERVER_DB_NAME_KEY = "database.h2.managedServer.name";
    String H2_MANAGED_SERVER_DB_PORT_KEY = "database.h2.managedServer.port";
    String H2_MANAGED_SERVER_DB_USERNAME_KEY = "database.h2.managedServer.username";
    String H2_MANAGED_SERVER_DB_PASS_KEY = "database.h2.managedServer.password";
    String H2_MANAGED_SERVER_DB_FILE_PASS_KEY = "database.h2.managedServer.filePassword";
    String H2_MANAGED_SERVER_DB_CONNECTION_TYPE_KEY = "database.h2.managedServer.connectionType";
    String H2_MANAGED_SERVER_DB_ENCRYPTION_ALGORITHM_KEY = "database.h2.managedServer.encryptionAlgorithm";

    @Key(DB_MIN_IDLE_KEY)
    int minimumIdle();

    @Key(DB_MAX_POOL_KEY)
    int maxPool();

    @Key(DB_CONN_TIMEOUT_MS_KEY)
    int connectionTimeoutMilliseconds();

    @Key(DB_CONN_MAX_LIFETIME_MS_KEY)
    long connectionMaxLifetimeMilliseconds();

    @Key(DB_IDLE_TIMEOUT_MS_KEY)
    long idleTimeoutMilliseconds();

    @Key(DB_LEAK_DETECTION_THRESHOLD_KEY)
    long leakDetectionThreshold();

    @Key(DB_VALIDATION_TIMEOUT_MS_KEY)
    int validationTimeoutMilliseconds();

    @Key(DB_INIT_FAIL_FAST_KEY)
    boolean failFastDuringInit();

    @Key(DB_DROP_DURING_START_KEY)
    boolean dropDbDuringStart();

    @Key(DB_AUDIT_ENABLED_KEY)
    boolean auditEnabled();

    @Key(H2_MANAGED_SERVER_DB_NAME_KEY)
    String h2ManagedServerDbName();

    @Key(H2_MANAGED_SERVER_DB_PORT_KEY)
    Integer h2ManagedServerDbPort();

    @Key(H2_MANAGED_SERVER_DB_USERNAME_KEY)
    String h2ManagedServerUsername();

    @Key(H2_MANAGED_SERVER_DB_PASS_KEY)
    String h2ManagedServerPassword();

    @Key(H2_MANAGED_SERVER_DB_FILE_PASS_KEY)
    String h2ManagedServerFilePassword();

    @Key(H2_MANAGED_SERVER_DB_CONNECTION_TYPE_KEY)
    String h2ManagedServerConnectionType();

    @Key(H2_MANAGED_SERVER_DB_ENCRYPTION_ALGORITHM_KEY)
    String h2ManagedServerDbEncryptionAlgorithm();
}
