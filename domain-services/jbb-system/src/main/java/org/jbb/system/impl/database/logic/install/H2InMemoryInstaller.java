/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.logic.install;

import org.jbb.install.database.DatabaseInstallationData;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.springframework.stereotype.Component;

@Component
public class H2InMemoryInstaller implements DbProviderInstaller {

    @Override
    public boolean isApplicable(DatabaseProvider databaseProvider) {
        return databaseProvider == DatabaseProvider.H2_IN_MEMORY;
    }

    @Override
    public void apply(DatabaseInstallationData dbInstallData, DatabaseSettings databaseSettings) {
        // nothing to do...
    }

}
