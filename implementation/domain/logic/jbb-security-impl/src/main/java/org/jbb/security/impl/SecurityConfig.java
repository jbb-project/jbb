/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl;

import org.jbb.lib.db.DbConfig;
import org.jbb.lib.properties.ModulePropertiesFactory;
import org.jbb.members.api.service.MemberService;
import org.jbb.security.impl.password.dao.PasswordRepository;
import org.jbb.security.impl.password.data.PasswordProperties;
import org.jbb.security.impl.userdetails.logic.SecurityContentUserFactory;
import org.jbb.security.impl.userdetails.logic.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(
        basePackages = {"org.jbb.security.impl.password.dao", "org.jbb.security.impl.role.dao"},
        entityManagerFactoryRef = DbConfig.EM_FACTORY_BEAN_NAME,
        transactionManagerRef = DbConfig.JTA_MANAGER_BEAN_NAME)
@EnableTransactionManagement
@ComponentScan("org.jbb.security.impl")
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordProperties securityProperties(ModulePropertiesFactory propertiesFactory) {
        return propertiesFactory.create(PasswordProperties.class);
    }

    @Bean
    public UserDetailsService userDetailsService(MemberService memberService, PasswordRepository passwordRepository,
                                                 SecurityContentUserFactory securityContentUserFactory) {
        return new UserDetailsServiceImpl(memberService, passwordRepository, securityContentUserFactory);
    }

    @Bean
    public DaoAuthenticationProvider authProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
