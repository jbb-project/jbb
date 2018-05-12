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


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class DbPropertiesTest {

    @Test
    public void callDbPropertiesConstants() throws Exception {
        assertThat(DbProperties.DB_MIN_IDLE_KEY).isNotBlank();
        assertThat(DbProperties.DB_MAX_POOL_KEY).isNotBlank();
        assertThat(DbProperties.DB_CONN_TIMEOUT_MS_KEY).isNotBlank();
        assertThat(DbProperties.DB_CONN_MAX_LIFETIME_MS_KEY).isNotBlank();
        assertThat(DbProperties.DB_IDLE_TIMEOUT_MS_KEY).isNotBlank();
        assertThat(DbProperties.DB_LEAK_DETECTION_THRESHOLD_MS_KEY).isNotBlank();
        assertThat(DbProperties.DB_LEAK_DETECTION_THRESHOLD_MS_KEY).isNotBlank();
        assertThat(DbProperties.DB_VALIDATION_TIMEOUT_MS_KEY).isNotBlank();
        assertThat(DbProperties.DB_INIT_FAIL_FAST_KEY).isNotBlank();
        assertThat(DbProperties.DB_STATISTICS_ENABLED_KEY).isNotBlank();
        assertThat(DbProperties.DB_AUDIT_ENABLED_KEY).isNotBlank();

        assertThat(DbProperties.DB_CURRENT_PROVIDER).isNotBlank();

        assertThat(DbProperties.H2_IN_MEMORY_DB_NAME_KEY).isNotBlank();

        assertThat(DbProperties.H2_MANAGED_SERVER_DB_NAME_KEY).isNotBlank();
        assertThat(DbProperties.H2_MANAGED_SERVER_DB_PORT_KEY).isNotBlank();
        assertThat(DbProperties.H2_MANAGED_SERVER_DB_USERNAME_KEY).isNotBlank();
        assertThat(DbProperties.H2_MANAGED_SERVER_DB_PASS_KEY).isNotBlank();
        assertThat(DbProperties.H2_MANAGED_SERVER_DB_FILE_PASS_KEY).isNotBlank();
        assertThat(DbProperties.H2_MANAGED_SERVER_DB_CONNECTION_TYPE_KEY).isNotBlank();
        assertThat(DbProperties.H2_MANAGED_SERVER_DB_ENCRYPTION_ALGORITHM_KEY).isNotBlank();

        assertThat(DbProperties.H2_EMBEDDED_DB_NAME_KEY).isNotBlank();
        assertThat(DbProperties.H2_EMBEDDED_DB_USERNAME_KEY).isNotBlank();
        assertThat(DbProperties.H2_EMBEDDED_DB_PASS_KEY).isNotBlank();
        assertThat(DbProperties.H2_EMBEDDED_DB_FILE_PASS_KEY).isNotBlank();
        assertThat(DbProperties.H2_EMBEDDED_DB_ENCRYPTION_ALGORITHM_KEY).isNotBlank();

        assertThat(DbProperties.H2_REMOTE_SERVER_DB_URL_KEY).isNotBlank();
        assertThat(DbProperties.H2_REMOTE_SERVER_DB_USERNAME_KEY).isNotBlank();
        assertThat(DbProperties.H2_REMOTE_SERVER_DB_PASS_KEY).isNotBlank();
        assertThat(DbProperties.H2_REMOTE_SERVER_DB_FILE_PASS_KEY).isNotBlank();
        assertThat(DbProperties.H2_REMOTE_SERVER_DB_CONNECTION_TYPE_KEY).isNotBlank();
        assertThat(DbProperties.H2_REMOTE_SERVER_DB_ENCRYPTION_ALGORITHM_KEY).isNotBlank();

    }
}