/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.database.postgres;

import javax.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseProviderSettings;

@Getter
@Setter
@Builder
public class PostgresqlSettings implements DatabaseProviderSettings {

    @NotBlank
    private String hostName;

    @Min(1)
    private int port;

    @NotBlank
    private String databaseName;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Override
    public DatabaseProvider getDatabaseProvider() {
        return DatabaseProvider.POSTGRESQL;
    }
}
