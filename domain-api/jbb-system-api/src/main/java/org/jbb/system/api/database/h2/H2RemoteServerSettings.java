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

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.jbb.system.api.database.DatabaseProvider;

@Getter
@Setter
public class H2RemoteServerSettings extends H2ServerCommonSettings {

    @NotBlank
    private String url;

    @Override
    public DatabaseProvider getDatabaseProvider() {
        return DatabaseProvider.H2_REMOTE_SERVER;
    }
}
