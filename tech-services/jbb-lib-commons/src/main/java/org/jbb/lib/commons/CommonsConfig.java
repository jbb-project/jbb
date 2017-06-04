/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.commons;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@ComponentScan("org.jbb.lib.commons")
public class CommonsConfig {

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
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        PlatformResourceBundleLocator rbLocator =
                new PlatformResourceBundleLocator(ResourceBundleMessageInterpolator.USER_VALIDATION_MESSAGES, null, true);
        LocalValidatorFactoryBean validFactory = new LocalValidatorFactoryBean();
        validFactory.setMessageInterpolator(new ResourceBundleMessageInterpolator(rbLocator));
        return validFactory;
    }
}
