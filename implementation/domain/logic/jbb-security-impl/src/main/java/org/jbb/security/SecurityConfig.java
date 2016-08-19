/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security;

import com.google.common.collect.Sets;

import org.jbb.lib.db.JbbEntityManagerFactory;
import org.jbb.security.dao.SecurityAccountDetailsRepository;
import org.jbb.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(
        basePackages = {"org.jbb.security.dao"},
        entityManagerFactoryRef = "securityEntityManagerFactory",
        transactionManagerRef = SecurityConfig.JTA_MANAGER)
@EnableTransactionManagement
@ComponentScan("org.jbb.security")
public class SecurityConfig {
    public static final String JTA_MANAGER = "securityTransactionManager";

    @Bean
    public LocalContainerEntityManagerFactoryBean securityEntityManagerFactory(JbbEntityManagerFactory emFactory) {
        return emFactory.getNewInstance(Sets.newHashSet("org.jbb.security.entities"));
    }

    @Bean(name = JTA_MANAGER)
    JpaTransactionManager securityTransactionManager(@Qualifier("securityEntityManagerFactory") EntityManagerFactory emFactory) {
        JpaTransactionManager jtaManager = new JpaTransactionManager();
        jtaManager.setEntityManagerFactory(emFactory);
        return jtaManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(SecurityAccountDetailsRepository repository) {
        return new UserDetailsServiceImpl(repository);
    }

    @Bean
    public DaoAuthenticationProvider authProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
