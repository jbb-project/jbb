/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.database.h2;

import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseProviderSettings;

import javax.validation.constraints.NotBlank;

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
public class H2InMemorySettings implements DatabaseProviderSettings {

    @NotBlank
    private String databaseName;

    @Override
    public DatabaseProvider getDatabaseProvider() {
        return DatabaseProvider.H2_IN_MEMORY;
    }
}
