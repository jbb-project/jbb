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

import org.jbb.frontend.impl.acp.AcpCategoryFactory;
import org.jbb.frontend.impl.acp.AcpSubcategoryFactory;
import org.jbb.frontend.impl.acp.AcpSubcategoryFactory.AcpElementTuple;
import org.jbb.frontend.impl.acp.dao.AcpCategoryRepository;
import org.jbb.frontend.impl.acp.model.AcpCategoryEntity;
import org.jbb.frontend.impl.acp.model.AcpSubcategoryEntity;
import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.install.JbbVersions;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import static org.jbb.frontend.impl.acp.AcpConstants.INTEGRATION_SUBCATEGORY;
import static org.jbb.frontend.impl.acp.AcpConstants.OAUTH_CLIENT_MANAGEMENT_ELEMENT;
import static org.jbb.frontend.impl.acp.AcpConstants.OAUTH_CLIENT_MANAGEMENT_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.SYSTEM_VIEW;

@Component
@RequiredArgsConstructor
public class AcpOAuthClientInstallAction implements InstallUpdateAction {

    private final AcpCategoryFactory acpCategoryFactory;
    private final AcpSubcategoryFactory acpSubcategoryFactory;

    private final AcpCategoryRepository acpCategoryRepository;

    @Override
    public Version fromVersion() {
        return JbbVersions.VERSION_0_12_0;
    }

    @Override
    public void install(InstallationData installationData) {
        AcpCategoryEntity categoryEntity = acpCategoryRepository.findByViewName(SYSTEM_VIEW);
        AcpSubcategoryEntity newSubcategory = acpSubcategoryFactory
                .createWithElements(INTEGRATION_SUBCATEGORY,
                        new AcpElementTuple(OAUTH_CLIENT_MANAGEMENT_ELEMENT, OAUTH_CLIENT_MANAGEMENT_VIEW)
                );
        acpCategoryFactory.addLastSubcategory(categoryEntity, newSubcategory);

        acpCategoryRepository.save(categoryEntity);
    }

}
