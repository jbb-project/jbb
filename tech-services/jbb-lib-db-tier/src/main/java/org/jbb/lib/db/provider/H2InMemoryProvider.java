/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db.provider;

import org.apache.commons.lang3.NotImplementedException;
import org.jbb.lib.db.DbProperties;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class H2InMemoryProvider extends H2AbstractProvider {

    public static final String PROVIDER_VALUE = "h2-in-memory";

    private static final String H2_MEMORY_PREFIX = "jdbc:h2:mem:";
    private static final String H2_MEMORY_SUFFIX = ";DB_CLOSE_DELAY=-1";

    private final DbProperties dbProperties;

    @Override
    public String getJdbcUrl() {
        return H2_MEMORY_PREFIX + dbProperties.h2InMemoryDbName() + H2_MEMORY_SUFFIX;
    }

    @Override
    public String getUsername() {
        return "jbbh2mem"; //NOSONAR
    }

    @Override
    public String getPassword() {
        return "jbbh2mem"; //NOSONAR
    }

    @Override
    String getEncryptionAlgorithm() {
        throw new NotImplementedException("Not needed");
    }
}
