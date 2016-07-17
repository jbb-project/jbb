/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members;

import com.google.common.collect.Sets;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.jbb.lib.db.JbbEntityManagerFactory;
import org.jbb.lib.properties.ModulePropertiesFactory;
import org.jbb.members.properties.MembersProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(
        basePackages = {"org.jbb.members.dao"},
        entityManagerFactoryRef = "membersEntityManagerFactory",
        transactionManagerRef = MembersConfig.JTA_MANAGER)
@ComponentScan("org.jbb.members")
public class MembersConfig {
    public static final String JTA_MANAGER = "membersTransactionManager";

    @Autowired
    private JbbEntityManagerFactory emFactory;

    @Autowired
    private ModulePropertiesFactory propertiesFactory;

    @Bean
    public LocalContainerEntityManagerFactoryBean membersEntityManagerFactory() {
        return emFactory.getNewInstance(Sets.newHashSet("org.jbb.members.entities"));
    }

    @Bean(name = JTA_MANAGER)
    JpaTransactionManager membersTransactionManager(@Qualifier("membersEntityManagerFactory") EntityManagerFactory emFactory) {
        JpaTransactionManager jtaManager = new JpaTransactionManager();
        jtaManager.setEntityManagerFactory(emFactory);
        return jtaManager;
    }

    @Bean
    public MembersProperties membersProperties() {
        return propertiesFactory.create(MembersProperties.class);
    }

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        PlatformResourceBundleLocator rbLocator =
                new PlatformResourceBundleLocator(ResourceBundleMessageInterpolator.USER_VALIDATION_MESSAGES, null, true);
        LocalValidatorFactoryBean validFactory = new LocalValidatorFactoryBean();
        validFactory.setMessageInterpolator(new ResourceBundleMessageInterpolator(rbLocator));
        return validFactory;
    }
}
