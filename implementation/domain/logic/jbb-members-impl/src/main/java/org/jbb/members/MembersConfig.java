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

import org.jbb.lib.db.JbbEntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(
        basePackages = {"org.jbb.members.dao"},
        entityManagerFactoryRef = "membersEntityManagerFactory",
        transactionManagerRef = MembersConfig.TRANSACTION_MGR_NAME)
@ComponentScan("org.jbb.members")
public class MembersConfig {
    public static final String TRANSACTION_MGR_NAME = "membersTransactionManager";

    @Autowired
    private JbbEntityManagerFactory emFactory;

    @Bean
    public LocalContainerEntityManagerFactoryBean membersEntityManagerFactory() {
        return emFactory.foo(Sets.newHashSet("org.jbb.members.entities"));
    }

    @Bean(name = TRANSACTION_MGR_NAME)
    JpaTransactionManager membersTransactionManager(EntityManagerFactory mainEntityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(mainEntityManagerFactory);
        return transactionManager;
    }
}
