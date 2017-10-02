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

import lombok.RequiredArgsConstructor;
import org.jbb.lib.commons.JbbMetaData;
import org.jbb.lib.db.DbProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class H2ManagedServerProvider extends H2AbstractProvider {

    public static final String PROVIDER_VALUE = "h2-managed-server";

    private static final String H2_TCP_PREFIX = "jdbc:h2:tcp://localhost:";
    private static final String H2_SSL_PREFIX = "jdbc:h2:ssl://localhost:";

    private final JbbMetaData jbbMetaData;
    private final DbProperties dbProperties;

    @Override
    public String getJdbcUrl() {
        return String.format("%s/%s/%s/%s;%s",
            resolveServerPrefix() + dbProperties.h2ManagedServerDbPort(),
            jbbMetaData.jbbHomePath(),
            DB_SUBDIR_NAME,
            dbProperties.h2ManagedServerDbName(),
            resolveCipher());
    }

    @Override
    public String getUsername() {
        return dbProperties.h2ManagedServerUsername();
    }

    @Override
    public String getPassword() {
        if (resolveCipher().isEmpty()) {
            return dbProperties.h2ManagedServerPassword();
        } else {
            return dbProperties.h2ManagedServerFilePassword() + " " + dbProperties
                .h2ManagedServerPassword();
        }
    }

    private String resolveServerPrefix() {
        String connectionType = dbProperties.h2ManagedServerConnectionType();
        if ("tcp".equalsIgnoreCase(connectionType)) {
            return H2_TCP_PREFIX;
        } else if ("ssl".equalsIgnoreCase(connectionType)) {
            return H2_SSL_PREFIX;
        }
        throw new IllegalArgumentException(
            "Incorrect h2 managed server connection type: " + connectionType);
    }

    @Override
    String getEncryptionAlgorithm() {
        return dbProperties.h2ManagedServerDbEncryptionAlgorithm();
    }

    public boolean isCurrentProvider() {
        return PROVIDER_VALUE.equalsIgnoreCase(dbProperties.currentProvider());
    }
}
