/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiObjectFactoryBean;

import java.util.Optional;

import javax.naming.NamingException;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class CoreConfig {
    public static final String JNDI_NAME = "jbb/home";
    public static final String JNDI_JBB_HOME_PATH_BEAN = "jndiJbbHomePath";

    @Bean(name = JNDI_JBB_HOME_PATH_BEAN)
    public Optional<String> jndiJbbHomePath() {
        try {
            JndiObjectFactoryBean jndiFactory = new JndiObjectFactoryBean();
            jndiFactory.setJndiName(JNDI_NAME);
            jndiFactory.setResourceRef(true);
            jndiFactory.setExpectedType(String.class);
            jndiFactory.afterPropertiesSet();
            return Optional.ofNullable((String) jndiFactory.getObject());
        } catch (NamingException e) {
            log.info("Value of '{}' property not found in JNDI", JNDI_NAME);
            return Optional.empty();
        }
    }

    @Bean
    public JbbHomePath jbbHomePath() {
        JbbHomePath jbbHomePath = new JbbHomePath(jndiJbbHomePath());
        jbbHomePath.resolveEffectiveAndStoreToSystemProperty();
        jbbHomePath.createIfNotExists();
        return jbbHomePath;
    }

    @Bean
    public JbbMetaData jbbMetaData(JbbHomePath jbbHomePath) {
        return new JbbMetaData(jbbHomePath);
    }
}
