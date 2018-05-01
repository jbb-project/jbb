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

import org.apache.commons.lang3.StringUtils;

public abstract class H2AbstractProvider implements DatabaseProvider {

    public static final String DB_SUBDIR_NAME = "db";


    @Override
    public String getDriverClass() {
        return "org.h2.Driver"; //NOSONAR
    }

    @Override
    public String getHibernateDialectName() {
        return "org.hibernate.dialect.H2Dialect"; //NOSONAR
    }

    abstract String getEncryptionAlgorithm();

    String resolveCipher() {
        String encryptionAlgorithm = getEncryptionAlgorithm();
        if (StringUtils.isNotBlank(encryptionAlgorithm)) {
            return "cipher=" + encryptionAlgorithm.toLowerCase();
        } else {
            return StringUtils.EMPTY;
        }
    }

}
