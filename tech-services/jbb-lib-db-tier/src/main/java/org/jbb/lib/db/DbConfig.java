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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.persistence.EntityManagerFactory;
import liquibase.integration.spring.SpringLiquibase;
import org.jbb.lib.cache.CacheConfig;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.commons.JbbMetaData;
import org.jbb.lib.db.health.ConnectionPoolHealthCheck;
import org.jbb.lib.db.health.DatabaseHealthCheck;
import org.jbb.lib.db.provider.DatabaseProviderService;
import org.jbb.lib.db.provider.H2ManagedServerProvider;
import org.jbb.lib.properties.ModulePropertiesFactory;
import org.jbb.lib.properties.PropertiesConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;

@Configuration
@ComponentScan
@Import({CommonsConfig.class, PropertiesConfig.class, CacheConfig.class})
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
                                                             ProxyEntityManagerFactory proxyEntityManagerFactory,
        SpringLiquibase springLiquibase, H2ManagedTcpServerManager h2ManagedTcpServerManager,
        DatabaseHealthCheck databaseHealthCheck,
        ConnectionPoolHealthCheck connectionPoolHealthCheck) {
        DbPropertyChangeListener listener = new DbPropertyChangeListener(proxyDataSource, dataSourceFactoryBean,
                jbbEntityManagerFactory, proxyEntityManagerFactory, springLiquibase,
            h2ManagedTcpServerManager, databaseHealthCheck, connectionPoolHealthCheck);
        dbProperties.addPropertyChangeListener(listener);
        return listener;
    }

    @Bean
    public DbProperties dbProperties(ModulePropertiesFactory propertiesFactory) {
        return propertiesFactory.create(DbProperties.class);
    }

    @Bean
    public SpringLiquibase springLiquibase(CloseableProxyDataSource mainDataSource) {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setDataSource(mainDataSource);
        springLiquibase.setChangeLog("classpath:liquibase/jbb-db-changelog-root.xml");
        return springLiquibase;
    }

    @Bean
    public DataSourceFactoryBean dataSourceFactoryBean(DbProperties dbProperties,
                                                       DatabaseProviderService databaseProviderService) {
        return new DataSourceFactoryBean(dbProperties, databaseProviderService);
    }

    @Bean(destroyMethod = "close")
    @DependsOn("h2ManagedTcpServerManager")
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
    H2ManagedTcpServerManager h2ManagedTcpServerManager(DbProperties dbProperties,
                                                        H2ManagedServerProvider h2ManagedServerProvider) {
        return new H2ManagedTcpServerManager(dbProperties, h2ManagedServerProvider);
    }

}
