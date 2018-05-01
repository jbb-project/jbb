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

import org.jbb.lib.db.DbProperties;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class H2RemoteServerProvider extends H2AbstractProvider {

    public static final String PROVIDER_VALUE = "h2-remote-server";

    private static final String H2_TCP_PREFIX = "jdbc:h2:tcp:";
    private static final String H2_SSL_PREFIX = "jdbc:h2:ssl:";

    private final DbProperties dbProperties;

    @Override
    public String getJdbcUrl() {
        return String.format("%s//%s;%s",
                resolveServerPrefix(),
                dbProperties.h2RemoteServerDbUrl(),
                resolveCipher());
    }

    @Override
    public String getUsername() {
        return dbProperties.h2RemoteServerUsername();
    }

    @Override
    public String getPassword() {
        if (resolveCipher().isEmpty()) {
            return dbProperties.h2RemoteServerPassword();
        } else {
            return dbProperties.h2RemoteServerFilePassword() + " " + dbProperties
                    .h2RemoteServerPassword();
        }
    }

    private String resolveServerPrefix() {
        String connectionType = dbProperties.h2RemoteServerConnectionType();
        if ("tcp".equalsIgnoreCase(connectionType)) {
            return H2_TCP_PREFIX;
        } else if ("ssl".equalsIgnoreCase(connectionType)) {
            return H2_SSL_PREFIX;
        }
        throw new IllegalArgumentException(
                "Incorrect h2 remote server connection type: " + connectionType);
    }

    @Override
    String getEncryptionAlgorithm() {
        return dbProperties.h2RemoteServerDbEncryptionAlgorithm();
    }
}
