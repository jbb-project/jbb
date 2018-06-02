/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.database;

import org.jbb.system.api.database.DatabaseProvider;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@ApiModel("DatabaseSettingsRequest")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EditDatabaseSettingsDto {

    private CommonDatabaseSettingsDto commonSettings;

    private H2InMemorySettingsDto h2InMemorySettings;

    private EditH2EmbeddedSettingsDto h2EmbeddedSettings;

    private EditH2ManagedServerSettingsDto h2ManagedServerSettings;

    private EditH2RemoteServerSettingsDto h2RemoteServerSettings;

    private EditPostgresqlSettingsDto postgresqlSettings;

    private DatabaseProvider currentDatabaseProvider;
}
