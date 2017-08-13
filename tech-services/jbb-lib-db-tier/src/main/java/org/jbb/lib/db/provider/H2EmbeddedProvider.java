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
public class H2EmbeddedProvider extends H2AbstractProvider {

    public static final String PROVIDER_VALUE = "h2-embedded";

    private static final String H2_FILE_PREFIX = "jdbc:h2:file:";

    private final JbbMetaData jbbMetaData;
    private final DbProperties dbProperties;

    @Override
    public String getJdbcUrl() {
        return String.format("%s%s/%s/%s;%s",
            H2_FILE_PREFIX,
            jbbMetaData.jbbHomePath(),
            DB_SUBDIR_NAME,
            dbProperties.h2EmbeddedDbName(),
            resolveCipher());
    }

    @Override
    public String getUsername() {
        return dbProperties.h2EmbeddedUsername();
    }

    @Override
    public String getPassword() {
        if (resolveCipher().isEmpty()) {
            return dbProperties.h2EmbeddedPassword();
        } else {
            return dbProperties.h2EmbeddedFilePassword() + " " + dbProperties.h2EmbeddedPassword();
        }
    }

    @Override
    String getEncryptionAlgorithm() {
        return dbProperties.h2EmbeddedDbEncryptionAlgorithm();
    }
}
