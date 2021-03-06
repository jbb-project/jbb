/*
 * Copyright (C) 2018 the original author or authors.
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
@Sources({"file:${jbb.home}/config/database.properties"})
public interface DbProperties extends ModuleProperties { // NOSONAR (key names should stay)

    String DB_MIN_IDLE_KEY = "database.minimum.idle";
    String DB_MAX_POOL_KEY = "database.maximum.pool";
    String DB_CONN_TIMEOUT_MS_KEY = "database.connection.timeoutMilli";
    String DB_CONN_MAX_LIFETIME_MS_KEY = "database.connection.maxLifetimeMilli";
    String DB_IDLE_TIMEOUT_MS_KEY = "database.idle.timeoutMilli";
    String DB_LEAK_DETECTION_THRESHOLD_MS_KEY = "database.leakDetection.thresholdMilli";
    String DB_VALIDATION_TIMEOUT_MS_KEY = "database.validation.timeoutMilli";
    String DB_INIT_FAIL_FAST_KEY = "database.init.failFast";
    String DB_STATISTICS_ENABLED_KEY = "database.statistics.enabled";
    String DB_AUDIT_ENABLED_KEY = "database.audit.enabled";

    String DB_CURRENT_PROVIDER = "database.provider";

    String H2_IN_MEMORY_DB_NAME_KEY = "database.h2.inMemory.name";

    String H2_MANAGED_SERVER_DB_NAME_KEY = "database.h2.managedServer.name";
    String H2_MANAGED_SERVER_DB_PORT_KEY = "database.h2.managedServer.port";
    String H2_MANAGED_SERVER_DB_USERNAME_KEY = "database.h2.managedServer.username";
    String H2_MANAGED_SERVER_DB_PASS_KEY = "database.h2.managedServer.password";
    String H2_MANAGED_SERVER_DB_FILE_PASS_KEY = "database.h2.managedServer.filePassword";
    String H2_MANAGED_SERVER_DB_CONNECTION_TYPE_KEY = "database.h2.managedServer.connectionType";
    String H2_MANAGED_SERVER_DB_ENCRYPTION_ALGORITHM_KEY = "database.h2.managedServer.encryptionAlgorithm";

    String H2_EMBEDDED_DB_NAME_KEY = "database.h2.embedded.name";
    String H2_EMBEDDED_DB_USERNAME_KEY = "database.h2.embedded.username";
    String H2_EMBEDDED_DB_PASS_KEY = "database.h2.embedded.password";
    String H2_EMBEDDED_DB_FILE_PASS_KEY = "database.h2.embedded.filePassword";
    String H2_EMBEDDED_DB_ENCRYPTION_ALGORITHM_KEY = "database.h2.embedded.encryptionAlgorithm";

    String H2_REMOTE_SERVER_DB_URL_KEY = "database.h2.remoteServer.url";
    String H2_REMOTE_SERVER_DB_USERNAME_KEY = "database.h2.remoteServer.username";
    String H2_REMOTE_SERVER_DB_PASS_KEY = "database.h2.remoteServer.password";
    String H2_REMOTE_SERVER_DB_FILE_PASS_KEY = "database.h2.remoteServer.filePassword";
    String H2_REMOTE_SERVER_DB_CONNECTION_TYPE_KEY = "database.h2.remoteServer.connectionType";
    String H2_REMOTE_SERVER_DB_ENCRYPTION_ALGORITHM_KEY = "database.h2.remoteServer.encryptionAlgorithm";

    String POSTGRESQL_HOST_KEY = "database.postgres.host";
    String POSTGRESQL_PORT_KEY = "database.postgres.port";
    String POSTGRESQL_DB_NAME_KEY = "database.postgres.databaseName";
    String POSTGRESQL_USERNAME_KEY = "database.postgres.username";
    String POSTGRESQL_PASS_KEY = "database.postgres.password";

    @Key(DB_MIN_IDLE_KEY)
    int minimumIdle();

    @Key(DB_MAX_POOL_KEY)
    int maxPool();

    @Key(DB_CONN_TIMEOUT_MS_KEY)
    int connectionTimeoutMilliseconds();

    @Key(DB_CONN_MAX_LIFETIME_MS_KEY)
    int connectionMaxLifetimeMilliseconds();

    @Key(DB_IDLE_TIMEOUT_MS_KEY)
    int idleTimeoutMilliseconds();

    @Key(DB_LEAK_DETECTION_THRESHOLD_MS_KEY)
    int leakDetectionThresholdMilliseconds();

    @Key(DB_VALIDATION_TIMEOUT_MS_KEY)
    int validationTimeoutMilliseconds();

    @Key(DB_INIT_FAIL_FAST_KEY)
    boolean failFastDuringInit();

    @Key(DB_STATISTICS_ENABLED_KEY)
    boolean statisticsEnabled();

    @Key(DB_AUDIT_ENABLED_KEY)
    boolean auditEnabled();

    @Key(DB_CURRENT_PROVIDER)
    String currentProvider();

    @Key(H2_IN_MEMORY_DB_NAME_KEY)
    String h2InMemoryDbName();

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

    @Key(H2_EMBEDDED_DB_NAME_KEY)
    String h2EmbeddedDbName();

    @Key(H2_EMBEDDED_DB_USERNAME_KEY)
    String h2EmbeddedUsername();

    @Key(H2_EMBEDDED_DB_PASS_KEY)
    String h2EmbeddedPassword();

    @Key(H2_EMBEDDED_DB_FILE_PASS_KEY)
    String h2EmbeddedFilePassword();

    @Key(H2_EMBEDDED_DB_ENCRYPTION_ALGORITHM_KEY)
    String h2EmbeddedDbEncryptionAlgorithm();

    @Key(H2_REMOTE_SERVER_DB_URL_KEY)
    String h2RemoteServerDbUrl();

    @Key(H2_REMOTE_SERVER_DB_USERNAME_KEY)
    String h2RemoteServerUsername();

    @Key(H2_REMOTE_SERVER_DB_PASS_KEY)
    String h2RemoteServerPassword();

    @Key(H2_REMOTE_SERVER_DB_FILE_PASS_KEY)
    String h2RemoteServerFilePassword();

    @Key(H2_REMOTE_SERVER_DB_CONNECTION_TYPE_KEY)
    String h2RemoteServerConnectionType();

    @Key(H2_REMOTE_SERVER_DB_ENCRYPTION_ALGORITHM_KEY)
    String h2RemoteServerDbEncryptionAlgorithm();

    @Key(POSTGRESQL_HOST_KEY)
    String postgresqlHost();

    @Key(POSTGRESQL_PORT_KEY)
    Integer postgresqlPort();

    @Key(POSTGRESQL_DB_NAME_KEY)
    String postgresqlDatabaseName();

    @Key(POSTGRESQL_USERNAME_KEY)
    String postgresqlUsername();

    @Key(POSTGRESQL_PASS_KEY)
    String postgresqlPassword();
}
