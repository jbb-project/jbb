/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.install;

import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.system.api.install.AlreadyInstalledException;
import org.jbb.system.api.install.InstallationDataException;
import org.jbb.system.api.install.InstallationService;
import org.jbb.system.api.install.InstalledStep;
import org.jbb.system.impl.install.dao.InstalledStepRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultInstallationService implements InstallationService {

    private final List<InstallUpdateAction> installActions;
    private final InstallationFileManager installationFileManager;
    private final InstallActionManager installActionManager;

    private final InstalledStepRepository installedStepRepository;
    private final EventSender eventSender;

    private final Validator validator;

    @Override
    public boolean isInstalled() {
        return installationFileManager.installationFileExists();
    }

    @Override
    public void install(InstallationData installationData) {

        Set<ConstraintViolation<InstallationData>> violations = validator.validate(installationData);
        if (!violations.isEmpty()) {
            throw new InstallationDataException(violations);
        }

        if (isInstalled()) {
            throw new AlreadyInstalledException();
        }

        installActions.sort(Comparator.comparing(InstallUpdateAction::fromVersion));
        installActions.forEach(
                installAction -> installActionManager.install(installAction, installationData)
        );
        installationFileManager.createInstallationFile(installationData);
        eventSender.sentInstallEvent();
    }

    @Override
    public List<InstalledStep> getInstalledSteps() {
        return installedStepRepository.findAllByOrderByInstalledDateTimeAsc().stream()
                .map(InstalledStep.class::cast)
                .collect(Collectors.toList());
    }
}
