/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.install.database;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseInstallationData {

    private H2EmbeddedInstallationData h2EmbeddedInstallationData;

    private H2ManagedServerInstallationData h2ManagedServerInstallationData;

    private H2RemoteServerInstallationData h2RemoteServerInstallationData;

    private PostgresqlInstallationData postgresqlInstallationData;

    @NotNull
    private DatabaseType databaseType;

}
