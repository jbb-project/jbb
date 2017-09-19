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

import java.util.Properties;
import javax.sql.DataSource;
import javax.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.cache.CacheProperties;
import org.jbb.lib.db.provider.DatabaseProviderService;
import org.springframework.context.annotation.DependsOn;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@DependsOn({"proxyAwareCachingProvider", "cacheManager"})
public class JbbEntityManagerFactory {

    private final DataSource dataSource;
    private final ValidatorFactory factory;
    private final DbProperties dbProperties;
    private final CacheProperties cacheProperties;
    private final DatabaseProviderService databaseProviderService;

    public LocalContainerEntityManagerFactoryBean getNewInstance() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan("org.jbb");

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect",
            databaseProviderService.getCurrentProvider().getHibernateDialectName());
        jpaProperties.put("hibernate.hbm2ddl.auto", "validate");
        jpaProperties.put("hibernate.show_sql", false);
        jpaProperties.put("hibernate.format_sql", true);
        jpaProperties.put("hibernate.use_sql_comments", true);
        jpaProperties.put("org.hibernate.flushMode", "COMMIT");
        jpaProperties.put("hibernate.integration.envers.enabled", dbProperties.auditEnabled());
        jpaProperties.put("org.hibernate.envers.audit_table_suffix", "_AUD");
        jpaProperties.put("hibernate.enable_lazy_load_no_trans", true);
        jpaProperties.put("javax.persistence.validation.factory", factory);
        jpaProperties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.jcache.JCacheRegionFactory");
        jpaProperties.put("hibernate.javax.cache.provider", "org.jbb.lib.cache.ProxyAwareCachingProvider");
        jpaProperties.put("hibernate.cache.use_second_level_cache", cacheProperties.secondLevelCacheEnabled());
        jpaProperties.put("hibernate.cache.use_query_cache", cacheProperties.queryCacheEnabled());
        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }
}
