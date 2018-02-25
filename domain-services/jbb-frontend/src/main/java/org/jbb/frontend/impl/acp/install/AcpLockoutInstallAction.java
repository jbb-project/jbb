/*
 * Copyright (C) 2017 the original author or authors.
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
import org.jbb.frontend.impl.acp.AcpSubcategoryFactory.AcpElementTuple;
import org.jbb.frontend.impl.acp.dao.AcpSubcategoryRepository;
import org.jbb.frontend.impl.acp.model.AcpSubcategoryEntity;
import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.install.JbbVersions;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import static org.jbb.frontend.impl.acp.AcpConstants.BOARD_CONFIGURATION_SUBCATEGORY;
import static org.jbb.frontend.impl.acp.AcpConstants.GENERAL_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.LOCKOUT_SETTINGS_ELEMENT;
import static org.jbb.frontend.impl.acp.AcpConstants.LOCKOUT_SETTINGS_VIEW;

@Component
@RequiredArgsConstructor
public class AcpLockoutInstallAction implements InstallUpdateAction {

    private final AcpSubcategoryFactory acpSubcategoryFactory;

    private final AcpSubcategoryRepository acpSubcategoryRepository;

    @Override
    public Version fromVersion() {
        return JbbVersions.VERSION_0_7_0;
    }

    @Override
    public void install(InstallationData installationData) {
        AcpSubcategoryEntity subcategoryEntity = acpSubcategoryRepository
                .findByCategoryViewNameAndName(GENERAL_VIEW, BOARD_CONFIGURATION_SUBCATEGORY);
        acpSubcategoryFactory.addLastElement(subcategoryEntity,
                new AcpElementTuple(LOCKOUT_SETTINGS_ELEMENT, LOCKOUT_SETTINGS_VIEW));
        acpSubcategoryRepository.save(subcategoryEntity);
    }

}
