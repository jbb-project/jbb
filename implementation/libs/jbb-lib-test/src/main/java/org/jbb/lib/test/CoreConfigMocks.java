/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.test;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.jbb.lib.core.JbbMetaData;
import org.jbb.lib.core.security.UserDetailsSource;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.File;

import static org.mockito.Mockito.when;

@Configuration
public class CoreConfigMocks {

    @Bean
    @Primary
    public JbbMetaData jbbMetaData() {
        JbbMetaData metaDataMock = Mockito.mock(JbbMetaData.class);
        File tempDir = com.google.common.io.Files.createTempDir();
        when(metaDataMock.jbbHomePath()).thenReturn(tempDir.getAbsolutePath());
        System.setProperty("jbb.home", tempDir.getAbsolutePath());
        return metaDataMock;
    }

    @Bean
    @Primary
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        PlatformResourceBundleLocator rbLocator =
                new PlatformResourceBundleLocator(ResourceBundleMessageInterpolator.USER_VALIDATION_MESSAGES, null, true);
        LocalValidatorFactoryBean validFactory = new LocalValidatorFactoryBean();
        validFactory.setMessageInterpolator(new ResourceBundleMessageInterpolator(rbLocator));
        return validFactory;
    }

    @Bean
    @Primary
    public UserDetailsSource userDetailsSource() {
        return new UserDetailsSource();
    }
}
