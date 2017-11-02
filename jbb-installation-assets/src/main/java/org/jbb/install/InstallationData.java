/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.install;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jbb.install.cache.CacheInstallationData;
import org.jbb.install.database.DatabaseInstallationData;

@Getter
@Setter
@Builder
public class InstallationData {

    private String adminUsername;
    private String adminDisplayedName;
    private String adminEmail;
    private String adminPassword;

    private String boardName;

    private DatabaseInstallationData databaseInstallationData;

    private CacheInstallationData cacheInstallationData;

}
