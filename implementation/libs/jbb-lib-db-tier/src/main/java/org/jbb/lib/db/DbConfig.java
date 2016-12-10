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

import org.jbb.lib.core.JbbMetaData;
import org.jbb.lib.properties.ModulePropertiesFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.persistence.EntityManagerFactory;

@Configuration
@ComponentScan("org.jbb.lib.db")
public class DbConfig {
    public static final String EM_FACTORY_BEAN_NAME = "entityManagerFactory";
    public static final String JTA_MANAGER_BEAN_NAME = "transactionManager";

    private static final String DB_SUBDIR_NAME = "db";

    private static void prepareDirectory(JbbMetaData jbbMetaData) {
        String dbDirectory = jbbMetaData.jbbHomePath() + File.separator + DB_SUBDIR_NAME;
        try {
            if (!Paths.get(dbDirectory).toFile().exists()) {
                Files.createDirectory(Paths.get(dbDirectory));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Bean
    public DbPropertyChangeListener dbPropertyChangeListener(CloseableProxyDataSource proxyDataSource,
                                                             DataSourceFactoryBean dataSourceFactoryBean,
                                                             DbStaticProperties dbStaticProperties,
                                                             JbbEntityManagerFactory jbbEntityManagerFactory,
                                                             ProxyEntityManagerFactory proxyEntityManagerFactory) {
        DbPropertyChangeListener listener = new DbPropertyChangeListener(proxyDataSource, dataSourceFactoryBean, jbbEntityManagerFactory, proxyEntityManagerFactory);
        dbStaticProperties.addPropertyChangeListener(listener);
        return listener;
    }

    @Bean
    public DbStaticProperties dbProperties(ModulePropertiesFactory propertiesFactory) {
        return propertiesFactory.create(DbStaticProperties.class);
    }

    @Bean
    public DataSourceFactoryBean dataSourceFactoryBean(DbStaticProperties dbProperties, JbbMetaData jbbMetaData) {
        return new DataSourceFactoryBean(dbProperties, jbbMetaData);
    }

    @Bean(destroyMethod = "close")
    public CloseableProxyDataSource mainDataSource(DataSourceFactoryBean dataSourceFactoryBean, JbbMetaData jbbMetaData) {
        prepareDirectory(jbbMetaData);
        return new CloseableProxyDataSource(dataSourceFactoryBean.getObject());
    }

    @Primary
    @Bean(name = EM_FACTORY_BEAN_NAME)
    public ProxyEntityManagerFactory entityManagerFactory(EntityManagerFactory rawEntityManagerFactory) {
        ProxyEntityManagerFactory emFactory = new ProxyEntityManagerFactory();
        emFactory.setObjectBeingProxied(rawEntityManagerFactory);
        return emFactory;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean rawEntityManagerFactory(JbbEntityManagerFactory emFactory) {
        return emFactory.getNewInstance();
    }

    @Bean(name = JTA_MANAGER_BEAN_NAME)
    JpaTransactionManager transactionManager(EntityManagerFactory emFactory) {
        JpaTransactionManager jtaManager = new JpaTransactionManager();
        jtaManager.setEntityManagerFactory(emFactory);
        return jtaManager;
    }
}
