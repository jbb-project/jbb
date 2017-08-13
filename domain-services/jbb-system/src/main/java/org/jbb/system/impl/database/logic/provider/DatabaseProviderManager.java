/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.logic.provider;

import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseProviderSettings;
import org.jbb.system.api.database.DatabaseSettings;

public interface DatabaseProviderManager<T extends DatabaseProviderSettings> {

    DatabaseProvider getProviderName();

    T getCurrentProviderSettings();

    void setProviderSettings(DatabaseSettings newDatabaseSettings);

}
