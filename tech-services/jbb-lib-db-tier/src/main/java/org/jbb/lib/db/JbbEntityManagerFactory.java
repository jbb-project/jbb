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

import org.jbb.lib.cache.CacheProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;

import java.util.Properties;

import javax.sql.DataSource;
import javax.validation.ValidatorFactory;


@Component
@DependsOn({"proxyAwareCachingProvider", "cacheManager"})
public class JbbEntityManagerFactory {
    private DataSource dataSource;
    private ValidatorFactory factory;
    private DbProperties dbProperties;
    private CacheProperties cacheProperties;

    @Autowired
    public JbbEntityManagerFactory(DataSource dataSource, ValidatorFactory validatorFactory,
                                   DbProperties dbProperties, CacheProperties cacheProperties) {
        this.dataSource = dataSource;
        this.factory = validatorFactory;
        this.dbProperties = dbProperties;
        this.cacheProperties = cacheProperties;
    }

    public LocalContainerEntityManagerFactoryBean getNewInstance() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan("org.jbb");

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        jpaProperties.put("hibernate.hbm2ddl.auto", schemaDdlBehave());
        jpaProperties.put("hibernate.show_sql", false);
        jpaProperties.put("hibernate.format_sql", true);
        jpaProperties.put("hibernate.use_sql_comments", true);
        jpaProperties.put("org.hibernate.flushMode", "COMMIT");
        jpaProperties.put("hibernate.integration.envers.enabled", dbProperties.auditEnabled());
        jpaProperties.put("org.hibernate.envers.audit_table_suffix", "_AUDIT");
        jpaProperties.put("hibernate.enable_lazy_load_no_trans", true);
        jpaProperties.put("javax.persistence.validation.factory", factory);
        jpaProperties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.jcache.JCacheRegionFactory");
        jpaProperties.put("hibernate.javax.cache.provider", "org.jbb.lib.cache.ProxyAwareCachingProvider");
        jpaProperties.put("hibernate.cache.use_second_level_cache", cacheProperties.secondLevelCacheEnabled());
        jpaProperties.put("hibernate.cache.use_query_cache", cacheProperties.queryCacheEnabled());
        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }

    private String schemaDdlBehave() {
        return dbProperties.dropDbDuringStart() ? "create-drop" : "update";
    }
}