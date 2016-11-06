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

import org.jbb.lib.core.JbbMetaData;
import org.jbb.lib.properties.ModulePropertiesFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@ComponentScan("org.jbb.lib.db")
public class DbConfig {
    public static final String EM_FACTORY_BEAN_NAME = "entityManagerFactory";
    public static final String JTA_MANAGER_BEAN_NAME = "transactionManager";

    private static final String HSQLDB_PREFIX = "jdbc:hsqldb:file";
    private static final String DB_SUBDIR_NAME = "db";
    private static final String HSQLDB_CONF = "hsqldb.lock_file=false;shutdown=true";

    private static void prepareDirectory(JbbMetaData jbbMetaData) {
        String dbDirectory = jbbMetaData.jbbHomePath() + File.separator + DB_SUBDIR_NAME;
        try {
            if (Files.notExists(Paths.get(dbDirectory))) {
                Files.createDirectory(Paths.get(dbDirectory));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Bean
    public DbStaticProperties dbProperties(ModulePropertiesFactory propertiesFactory) {
        return propertiesFactory.create(DbStaticProperties.class);
    }

    @Bean(destroyMethod = "close")
    public DataSource mainDataSource(DbStaticProperties dbProperties, JbbMetaData jbbMetaData) {
        prepareDirectory(jbbMetaData);
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSourceConfig.setJdbcUrl(String.format("%s:%s/%s/%s;%s",
                HSQLDB_PREFIX, jbbMetaData.jbbHomePath(), DB_SUBDIR_NAME, dbProperties.dbFilename(), HSQLDB_CONF));
        dataSourceConfig.setUsername("SA");
        dataSourceConfig.setPassword("");
        dataSourceConfig.setInitializationFailFast(dbProperties.failFastDuringInit());
        dataSourceConfig.setMinimumIdle(dbProperties.minimumIdle());
        dataSourceConfig.setMaximumPoolSize(dbProperties.maxPool());
        dataSourceConfig.setConnectionTimeout(dbProperties.connectionTimeoutMiliseconds());
        return new LoggingProxyDataSource(new HikariDataSource(dataSourceConfig));
    }

    @Bean(name = EM_FACTORY_BEAN_NAME)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(JbbEntityManagerFactory emFactory) {
        return emFactory.getNewInstance();
    }

    @Bean(name = JTA_MANAGER_BEAN_NAME)
    JpaTransactionManager transactionManager(EntityManagerFactory emFactory) {
        JpaTransactionManager jtaManager = new JpaTransactionManager();
        jtaManager.setEntityManagerFactory(emFactory);
        return jtaManager;
    }
}
