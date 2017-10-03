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
import org.jbb.lib.db.DbProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostgresqlServerProvider implements DatabaseProvider {

    public static final String PROVIDER_VALUE = "postgresql";

    private static final String PSQL_PREFIX = "jdbc:postgresql:";

    private final DbProperties dbProperties;

    @Override
    public String getDriverClass() {
        return "org.postgresql.Driver";
    }

    @Override
    public String getJdbcUrl() {
        return String.format("%s//%s:%s/%s",
            PSQL_PREFIX,
            dbProperties.postgresqlHost(),
            dbProperties.postgresqlPort(),
            dbProperties.postgresqlDatabaseName()
        );
    }

    @Override
    public String getUsername() {
        return dbProperties.postgresqlUsername();
    }

    @Override
    public String getPassword() {
        return dbProperties.postgresqlPassword();
    }

    @Override
    public String getHibernateDialectName() {
        return "org.hibernate.dialect.PostgreSQL95Dialect";
    }
}
