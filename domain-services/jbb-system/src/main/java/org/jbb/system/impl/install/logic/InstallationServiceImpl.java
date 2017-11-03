/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.install.logic;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jbb.install.InstallAction;
import org.jbb.install.InstallationData;
import org.jbb.system.api.install.InstallationService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstallationServiceImpl implements InstallationService {

    private final List<InstallAction> installActions;
    private final InstallationFileManager installationFileManager;

    @Override
    public boolean isInstalled() {
        return installationFileManager.installationFileExists();
    }

    @Override
    public void install(InstallationData installationData) {
        installActions.forEach(
            installAction -> installAction.install(installationData)
        );
        installationFileManager.createInstallationFile(installationData);
    }
}
