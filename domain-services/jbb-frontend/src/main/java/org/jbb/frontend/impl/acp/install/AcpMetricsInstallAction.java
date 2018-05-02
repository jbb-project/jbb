/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.acp.install;

import com.github.zafarkhaja.semver.Version;

import org.jbb.frontend.impl.acp.AcpSubcategoryFactory;
import org.jbb.frontend.impl.acp.dao.AcpSubcategoryRepository;
import org.jbb.frontend.impl.acp.model.AcpSubcategoryEntity;
import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.install.JbbVersions;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import static org.jbb.frontend.impl.acp.AcpConstants.MAINTENANCE_SUBCATEGORY;
import static org.jbb.frontend.impl.acp.AcpConstants.METRICS_SETTINGS_ELEMENT;
import static org.jbb.frontend.impl.acp.AcpConstants.METRICS_SETTINGS_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.SYSTEM_VIEW;

@Component
@RequiredArgsConstructor
public class AcpMetricsInstallAction implements InstallUpdateAction {

    private final AcpSubcategoryFactory acpSubcategoryFactory;

    private final AcpSubcategoryRepository acpSubcategoryRepository;

    @Override
    public Version fromVersion() {
        return JbbVersions.VERSION_0_11_0;
    }

    @Override
    public void install(InstallationData installationData) {
        AcpSubcategoryEntity subcategoryEntity = acpSubcategoryRepository
                .findByCategoryViewNameAndName(SYSTEM_VIEW, MAINTENANCE_SUBCATEGORY);
        acpSubcategoryFactory.addLastElement(subcategoryEntity,
                new AcpSubcategoryFactory.AcpElementTuple(METRICS_SETTINGS_ELEMENT, METRICS_SETTINGS_VIEW));
        acpSubcategoryRepository.save(subcategoryEntity);
    }
}
