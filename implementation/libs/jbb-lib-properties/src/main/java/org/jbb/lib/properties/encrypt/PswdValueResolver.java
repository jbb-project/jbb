/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties.encrypt;

import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Optional;

import javax.naming.NamingException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PswdValueResolver {
    public static final String JNDI_PSWD_NAME = "jbb/pswd";
    public static final String SYSTEM_PSWD_ENV = "JBB_PSWD";

    private Optional<String> propEncryptionPswd;

    public PswdValueResolver() {
        String jndiPswd = lookupInJndi();
        propEncryptionPswd = Optional.ofNullable(
                jndiPswd != null ? jndiPswd : lookupInSystemProperties()
        );
    }

    private String lookupInJndi() {
        try {
            JndiObjectFactoryBean jndiFactory = new JndiObjectFactoryBean();
            jndiFactory.setJndiName(JNDI_PSWD_NAME);
            jndiFactory.setResourceRef(true);
            jndiFactory.setExpectedType(String.class);
            jndiFactory.afterPropertiesSet();
            return (String) jndiFactory.getObject();
        } catch (NamingException e) {
            log.info("Value of '{}' property not found in JNDI", JNDI_PSWD_NAME);
            log.debug("Error while getting value from JNDI", e);
            return null;
        }
    }

    private String lookupInSystemProperties() {
        return System.getenv(SYSTEM_PSWD_ENV);
    }

    public Optional<String> getPassword() {
        return propEncryptionPswd;
    }
}
