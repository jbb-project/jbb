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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.commons.H2Settings;
import org.jbb.lib.commons.JbbMetaData;

@RequiredArgsConstructor
public class DataSourceFactoryBean {

    private static final String H2_FILE_PREFIX = "jdbc:h2:file:";
    private static final String H2_TCP_PREFIX = "jdbc:h2:tcp://localhost:";
    private static final String H2_SSL_PREFIX = "jdbc:h2:ssl://localhost:";

    private static final String DB_SUBDIR_NAME = "db";

    private final DbProperties dbProperties;
    private final JbbMetaData jbbMetaData;
    private final H2Settings h2Settings;

    public LoggingProxyDataSource getObject() {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName("org.h2.Driver");
        dataSourceConfig.setJdbcUrl(String.format("%s%s/%s/%s;%s",
            h2Settings.getMode() == H2Settings.Mode.SERVER ? resolveServerPrefix() + h2Settings.getPort() + "/" : H2_FILE_PREFIX,
            jbbMetaData.jbbHomePath(), DB_SUBDIR_NAME, dbProperties.h2ManagedServerDbName(), resolveCipher()));
        dataSourceConfig.setUsername(dbProperties.h2ManagedServerUsername());
        dataSourceConfig.setPassword(resolvePasswords());
        dataSourceConfig.setInitializationFailTimeout(dbProperties.failFastDuringInit() ? 1 : -1);
        dataSourceConfig.setMinimumIdle(dbProperties.minimumIdle());
        dataSourceConfig.setMaximumPoolSize(dbProperties.maxPool());
        dataSourceConfig.setConnectionTimeout(dbProperties.connectionTimeoutMilliseconds());
        dataSourceConfig.setMaxLifetime(dbProperties.connectionMaxLifetimeMilliseconds());
        dataSourceConfig.setIdleTimeout(dbProperties.idleTimeoutMilliseconds());
        dataSourceConfig.setLeakDetectionThreshold(dbProperties.leakDetectionThresholdMilliseconds());
        dataSourceConfig.setValidationTimeout(dbProperties.validationTimeoutMilliseconds());
        return new LoggingProxyDataSource(new HikariDataSource(dataSourceConfig));
    }

    private String resolveServerPrefix() {
        String connectionType = dbProperties.h2ManagedServerConnectionType();
        if ("tcp".equalsIgnoreCase(connectionType)) {
            return H2_TCP_PREFIX;
        } else if ("ssl".equalsIgnoreCase(connectionType)) {
            return H2_SSL_PREFIX;
        }
        throw new IllegalArgumentException("Incorrect h2 managed server connection type: " + connectionType);
    }

    private String resolvePasswords() {
        if (resolveCipher().isEmpty()) {
            return dbProperties.h2ManagedServerPassword();
        } else {
            return dbProperties.h2ManagedServerFilePassword() + " " + dbProperties.h2ManagedServerPassword();
        }
    }

    private String resolveCipher() {
        String encryptionAlgorithm = dbProperties.h2ManagedServerDbEncryptionAlgorithm();
        if (StringUtils.isNotBlank(encryptionAlgorithm)) {
            return "cipher=" + encryptionAlgorithm.toLowerCase();
        } else {
            return StringUtils.EMPTY;
        }
    }

}
