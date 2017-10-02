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
import org.jbb.lib.db.provider.DatabaseProvider;
import org.jbb.lib.db.provider.DatabaseProviderService;

@RequiredArgsConstructor
public class DataSourceFactoryBean {

    private final DbProperties dbProperties;
    private final DatabaseProviderService databaseProviderService;

    public LoggingProxyDataSource getObject() {
        DatabaseProvider currentProvider = databaseProviderService.getCurrentProvider();

        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName(currentProvider.getDriverClass());
        dataSourceConfig.setJdbcUrl(currentProvider.getJdbcUrl());
        dataSourceConfig.setUsername(currentProvider.getUsername());
        dataSourceConfig.setPassword(currentProvider.getPassword());
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

}
