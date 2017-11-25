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

import javax.naming.NamingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JndiValueReader {

    public String readValue(String jndiName) {
        try {
            JndiObjectFactoryBean jndiFactory = new JndiObjectFactoryBean();
            jndiFactory.setJndiName(jndiName);
            jndiFactory.setResourceRef(true);
            jndiFactory.setExpectedType(String.class);
            jndiFactory.afterPropertiesSet();
            return (String) jndiFactory.getObject();
        } catch (NamingException e) {
            log.info("Value of '{}' property not found in JNDI", jndiName);
            log.trace("Error while getting value from JNDI", e);
            return null;
        }
    }

}
