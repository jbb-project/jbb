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

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.system.api.install.InstallationException;
import org.jbb.system.impl.install.dao.InstalledStepRepository;
import org.jbb.system.impl.install.model.InstalledStepEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InstallActionManager {

    private final InstalledStepRepository installedStepRepository;

    public void install(InstallUpdateAction installAction, InstallationData installationData) {
        try {
            installAction.install(installationData);
        } catch (Exception e) {
            log.error("Installation aborted! Failed action: {}", installAction.getClass().getName(),
                e);
            throw new InstallationException(e);
        }
        installedStepRepository.save(buildInstalledStep(installAction));

    }

    private InstalledStepEntity buildInstalledStep(InstallUpdateAction installAction) {
        return InstalledStepEntity.builder()
            .name(installAction.getClass().getSimpleName())
            .fromVersion(installAction.fromVersion().toString())
            .installedDateTime(LocalDateTime.now())
            .build();
    }


}
