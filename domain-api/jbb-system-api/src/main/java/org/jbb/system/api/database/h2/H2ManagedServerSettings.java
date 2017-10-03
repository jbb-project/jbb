/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.database.h2;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.jbb.system.api.database.DatabaseProvider;

@Getter
@Setter
public class H2ManagedServerSettings extends H2ServerCommonSettings {

    @NotBlank
    private String databaseFileName;

    @Min(1)
    private int port;

    @Override
    public DatabaseProvider getDatabaseProvider() {
        return DatabaseProvider.H2_MANAGED_SERVER;
    }
}
