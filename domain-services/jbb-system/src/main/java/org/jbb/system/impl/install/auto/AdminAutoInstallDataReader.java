/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.install.auto;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.jbb.install.InstallationData;
import org.springframework.stereotype.Component;

@Component
public class AdminAutoInstallDataReader implements AutoInstallationDataReader {

    private static final String ADMIN_USERNAME = "admin.username";
    private static final String ADMIN_DISPLAYED_NAME = "admin.displayedName";
    private static final String ADMIN_EMAIL = "admin.email";
    private static final String ADMIN_PSWD = "admin.password";
    private static final String BOARD_NAME = "board.name";

    @Override
    public InstallationData updateInstallationData(InstallationData data,
                                                   FileBasedConfiguration configuration) {
        data.setAdminUsername(configuration.getString(ADMIN_USERNAME, null));
        data.setAdminDisplayedName(configuration.getString(ADMIN_DISPLAYED_NAME, null));
        data.setAdminEmail(configuration.getString(ADMIN_EMAIL, null));
        data.setAdminPassword(configuration.getString(ADMIN_PSWD, null));
        data.setBoardName(configuration.getString(BOARD_NAME, null));
        return data;
    }
}
