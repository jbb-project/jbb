/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties.encrypt;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jbb.lib.commons.JndiValueReader;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PswdValueResolver {
    public static final String JNDI_PSWD_NAME = "jbb/pswd";
    public static final String SYSTEM_PSWD_ENV = "JBB_PSWD";

    private final JndiValueReader jndiValueReader;

    private Optional<String> propEncryptionPswd;

    void resolvePassword() {
        String jndiPswd = jndiValueReader.readValue(JNDI_PSWD_NAME);
        propEncryptionPswd = Optional.ofNullable(
            jndiPswd == null ? lookupInSystemProperties() : jndiPswd
        );
    }

    private String lookupInSystemProperties() {
        return System.getenv(SYSTEM_PSWD_ENV);
    }

    public Optional<String> getPassword() {
        return propEncryptionPswd;
    }
}
