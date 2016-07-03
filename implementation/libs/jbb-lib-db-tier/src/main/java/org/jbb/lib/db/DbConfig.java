/*
 * Copyright (C) 2016 the original author or authors.
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

import org.jbb.lib.properties.JbbHomePath;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ComponentScan("org.jbb.lib.db")
public class DbConfig {
    @Bean(destroyMethod = "close")
    DataSource mainDataSource() {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSourceConfig.setJdbcUrl("jdbc:hsqldb:file:" + JbbHomePath.getEffective() + "/jbb-hsqldb-database.db");
        dataSourceConfig.setUsername("SA");
        dataSourceConfig.setPassword("");
        dataSourceConfig.setInitializationFailFast(false);
        dataSourceConfig.setMinimumIdle(5);
        dataSourceConfig.setMaximumPoolSize(10);
        dataSourceConfig.setConnectionTimeout(15000);
        return new HikariDataSource(dataSourceConfig);
    }
}