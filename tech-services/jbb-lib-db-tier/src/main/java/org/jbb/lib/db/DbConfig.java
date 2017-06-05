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

import org.jbb.lib.cache.CacheConfig;
import org.jbb.lib.commons.H2Settings;
import org.jbb.lib.commons.JbbMetaData;
import org.jbb.lib.properties.ModulePropertiesFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.persistence.EntityManagerFactory;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
@ComponentScan("org.jbb.lib.db")
@Import(CacheConfig.class)
public class DbConfig {
    public static final String EM_FACTORY_BEAN_NAME = "entityManagerFactory";
    public static final String JPA_MANAGER_BEAN_NAME = "transactionManager";

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
                                                             DbProperties dbProperties,
                                                             JbbEntityManagerFactory jbbEntityManagerFactory,
                                                             ProxyEntityManagerFactory proxyEntityManagerFactory, SpringLiquibase springLiquibase) {
        DbPropertyChangeListener listener = new DbPropertyChangeListener(proxyDataSource, dataSourceFactoryBean,
                jbbEntityManagerFactory, proxyEntityManagerFactory, springLiquibase);
        dbProperties.addPropertyChangeListener(listener);
        return listener;
    }

    @Bean
    public DbProperties dbProperties(ModulePropertiesFactory propertiesFactory) {
        return propertiesFactory.create(DbProperties.class);
    }

    @Bean
    public SpringLiquibase springLiquibase(CloseableProxyDataSource mainDataSource, DbProperties dbProperties) {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setDataSource(mainDataSource);
        springLiquibase.setChangeLog("classpath:jbb-db-changelog-root.xml");
        springLiquibase.setDropFirst(dbProperties.dropDbDuringStart());
        return springLiquibase;
    }

    @Bean
    public DataSourceFactoryBean dataSourceFactoryBean(DbProperties dbProperties, JbbMetaData jbbMetaData,
                                                       H2Settings h2Settings) {
        return new DataSourceFactoryBean(dbProperties, jbbMetaData, h2Settings);
    }

    @Bean(destroyMethod = "close")
    @DependsOn("embeddedDatabaseServerManager")
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
    @DependsOn("springLiquibase")
    public LocalContainerEntityManagerFactoryBean rawEntityManagerFactory(JbbEntityManagerFactory emFactory) {
        return emFactory.getNewInstance();
    }

    @Bean(name = JPA_MANAGER_BEAN_NAME)
    JpaTransactionManager transactionManager(EntityManagerFactory emFactory) {
        JpaTransactionManager jpaManager = new JpaTransactionManager();
        jpaManager.setEntityManagerFactory(emFactory);
        jpaManager.setJpaDialect(new HibernateJpaDialect());
        return jpaManager;
    }

    @Bean(destroyMethod = "stopH2Server")
    EmbeddedDatabaseServerManager embeddedDatabaseServerManager(DbProperties dbProperties, H2Settings h2Settings) {
        return new EmbeddedDatabaseServerManager(dbProperties, h2Settings);
    }

}
