/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.core;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.jbb.lib.core.security.UserDetailsSource;
import org.jbb.lib.core.time.JBBTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class CoreConfig {

    @Bean
    public JndiValueReader jndiValueReader() {
        return new JndiValueReader();
    }

    @Bean
    public JbbHomePath jbbHomePath(JndiValueReader jndiValueReader) {
        JbbHomePath jbbHomePath = new JbbHomePath(jndiValueReader);
        jbbHomePath.resolveEffective();
        jbbHomePath.createIfNotExists();
        return jbbHomePath;
    }

    @Bean
    public JbbMetaData jbbMetaData(JbbHomePath jbbHomePath) {
        return new JbbMetaData(jbbHomePath);
    }

    @Bean
    public UserDetailsSource userDetailsSource() {
        return new UserDetailsSource();
    }

    @Bean
    public JBBTime jbbTime() {
        return new JBBTime();
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
