/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.database;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jbb.system.api.database.h2.H2EmbeddedSettings;
import org.jbb.system.api.database.h2.H2ManagedServerSettings;

@Getter
@Setter
@Builder
public class DatabaseSettings {

    @NotNull
    @Valid
    private CommonDatabaseSettings commonSettings;

    @NotNull
    @Valid
    private H2EmbeddedSettings h2EmbeddedSettings;

    @NotNull
    @Valid
    private H2ManagedServerSettings h2ManagedServerSettings;

    @NotNull
    private DatabaseProvider currentDatabaseProvider;

}
