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

import org.jbb.lib.core.JbbMetaData;


public class DataSourceFactoryBean {
    private static final String HSQLDB_PREFIX = "jdbc:hsqldb:file";
    private static final String DB_SUBDIR_NAME = "db";
    private static final String HSQLDB_CONF = "hsqldb.lock_file=false;shutdown=true";

    private final DbProperties dbProperties;
    private final JbbMetaData jbbMetaData;

    public DataSourceFactoryBean(DbProperties dbProperties,
                                 JbbMetaData jbbMetaData) {
        this.dbProperties = dbProperties;
        this.jbbMetaData = jbbMetaData;
    }

    public LoggingProxyDataSource getObject() {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSourceConfig.setJdbcUrl(String.format("%s:%s/%s/%s;%s",
                HSQLDB_PREFIX, jbbMetaData.jbbHomePath(), DB_SUBDIR_NAME, dbProperties.dbFilename(), HSQLDB_CONF));
        dataSourceConfig.setUsername("SA");
        dataSourceConfig.setPassword("");
        dataSourceConfig.setInitializationFailTimeout(dbProperties.failFastDuringInit() ? 1 : -1);
        dataSourceConfig.setMinimumIdle(dbProperties.minimumIdle());
        dataSourceConfig.setMaximumPoolSize(dbProperties.maxPool());
        dataSourceConfig.setConnectionTimeout(dbProperties.connectionTimeoutMiliseconds());
        return new LoggingProxyDataSource(new HikariDataSource(dataSourceConfig));
    }

}
