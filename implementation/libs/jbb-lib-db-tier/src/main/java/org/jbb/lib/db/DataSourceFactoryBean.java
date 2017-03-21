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

import org.jbb.lib.core.H2Settings;
import org.jbb.lib.core.JbbMetaData;


public class DataSourceFactoryBean {
    private static final String H2_FILE_PREFIX = "jdbc:h2:file:";
    private static final String H2_TCP_PREFIX = "jdbc:h2:tcp://localhost/";

    private static final String DB_SUBDIR_NAME = "db";
    private static final String H2_CONF = "";

    private final DbProperties dbProperties;
    private final JbbMetaData jbbMetaData;
    private final H2Settings h2Settings;

    public DataSourceFactoryBean(DbProperties dbProperties,
                                 JbbMetaData jbbMetaData, H2Settings h2Settings) {
        this.dbProperties = dbProperties;
        this.jbbMetaData = jbbMetaData;
        this.h2Settings = h2Settings;
    }

    public LoggingProxyDataSource getObject() {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName("org.h2.Driver");
        dataSourceConfig.setJdbcUrl(String.format("%s%s/%s/%s;%s",
                h2Settings.getMode() == H2Settings.Mode.SERVER ? H2_TCP_PREFIX : H2_FILE_PREFIX,
                jbbMetaData.jbbHomePath(), DB_SUBDIR_NAME, dbProperties.dbFilename(), H2_CONF));
        dataSourceConfig.setUsername("jbb");
        dataSourceConfig.setPassword("jbb");
        dataSourceConfig.setInitializationFailTimeout(dbProperties.failFastDuringInit() ? 1 : -1);
        dataSourceConfig.setMinimumIdle(dbProperties.minimumIdle());
        dataSourceConfig.setMaximumPoolSize(dbProperties.maxPool());
        dataSourceConfig.setConnectionTimeout(dbProperties.connectionTimeoutMiliseconds());
        return new LoggingProxyDataSource(new HikariDataSource(dataSourceConfig));
    }

}
