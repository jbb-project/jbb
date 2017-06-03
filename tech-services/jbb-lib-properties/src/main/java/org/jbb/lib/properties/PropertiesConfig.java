/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jbb.lib.core.JbbMetaData;
import org.jbb.lib.properties.encrypt.PropertiesEncryption;
import org.jbb.lib.properties.encrypt.ReencryptionPropertyChangeListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("org.jbb.lib.properties")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class PropertiesConfig {

    @Bean
    public JbbPropertyFilesResolver jbbPropertyFilesResolver(JbbMetaData jbbMetaData) {
        return new JbbPropertyFilesResolver(jbbMetaData);
    }

    @Bean
    public FreshInstallPropertiesCreator freshInstallPropertiesCreator(JbbPropertyFilesResolver propertyFilesResolver) {
        return new FreshInstallPropertiesCreator(propertyFilesResolver);
    }

    @Bean
    public UpdateFilePropertyChangeListenerFactoryBean updateFilePropertyChangeListenerFactoryBean() {
        return new UpdateFilePropertyChangeListenerFactoryBean();
    }

    @Bean
    public ModulePropertiesFactory modulePropertiesFactory(
            FreshInstallPropertiesCreator propertiesCreator,
            UpdateFilePropertyChangeListenerFactoryBean updateFilePropertyChangeListenerFactoryBean,
            LoggingPropertyChangeListener loggingPropertyChangeListener,
            ReencryptionPropertyChangeListener reencryptionPropertyChangeListener) {
        return new ModulePropertiesFactory(propertiesCreator,
                updateFilePropertyChangeListenerFactoryBean,
                loggingPropertyChangeListener, reencryptionPropertyChangeListener);
    }

    @Bean
    public LoggingPropertyChangeListener loggingPropertyChangeListener() {
        return new LoggingPropertyChangeListener();
    }

    @Bean
    public ReencryptionPropertyChangeListener reencryptionPropertyChangeListener(PropertiesEncryption propertiesEncryption) {
        return new ReencryptionPropertyChangeListener(propertiesEncryption);
    }

    @Bean
    public StandardPBEStringEncryptor standardPBEStringEncryptor() {
        return new StandardPBEStringEncryptor();
    }
}