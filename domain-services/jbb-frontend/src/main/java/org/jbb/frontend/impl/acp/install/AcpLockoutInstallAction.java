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

import static org.jbb.frontend.impl.acp.AcpConstants.BOARD_CONFIGURATION_SUBCATEGORY;
import static org.jbb.frontend.impl.acp.AcpConstants.GENERAL_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.LOCKOUT_SETTINGS_ELEMENT;
import static org.jbb.frontend.impl.acp.AcpConstants.LOCKOUT_SETTINGS_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.MANAGE_MEMBER_LOCKS_ELEMENT;
import static org.jbb.frontend.impl.acp.AcpConstants.MANAGE_MEMBER_LOCKS_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.MEMBERS_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.MEMBER_LOCKS_SUBCATEGORY;

import com.github.zafarkhaja.semver.Version;
import lombok.RequiredArgsConstructor;
import org.jbb.frontend.impl.acp.AcpCategoryFactory;
import org.jbb.frontend.impl.acp.AcpSubcategoryFactory;
import org.jbb.frontend.impl.acp.AcpSubcategoryFactory.AcpElementTuple;
import org.jbb.frontend.impl.acp.dao.AcpCategoryRepository;
import org.jbb.frontend.impl.acp.dao.AcpSubcategoryRepository;
import org.jbb.frontend.impl.acp.model.AcpCategoryEntity;
import org.jbb.frontend.impl.acp.model.AcpSubcategoryEntity;
import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.install.JbbVersions;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AcpLockoutInstallAction implements InstallUpdateAction {

    private final AcpSubcategoryFactory acpSubcategoryFactory;

    private final AcpSubcategoryRepository acpSubcategoryRepository;

    private final AcpCategoryFactory acpCategoryFactory;

    private final AcpCategoryRepository acpCategoryRepository;

    @Override
    public Version fromVersion() {
        return JbbVersions.VERSION_0_7_0;
    }

    @Override
    public void install(InstallationData installationData) {
        // add lockout settings view
        AcpSubcategoryEntity subcategoryEntity = acpSubcategoryRepository
                .findByCategoryViewNameAndName(GENERAL_VIEW, BOARD_CONFIGURATION_SUBCATEGORY);
        acpSubcategoryFactory.addLastElement(subcategoryEntity,
                new AcpElementTuple(LOCKOUT_SETTINGS_ELEMENT, LOCKOUT_SETTINGS_VIEW));
        acpSubcategoryRepository.save(subcategoryEntity);

        // add lock browser view
        AcpCategoryEntity categoryEntity = acpCategoryRepository.findByViewName(MEMBERS_VIEW);
        AcpSubcategoryEntity newSubcategory = acpSubcategoryFactory
            .createWithElements(MEMBER_LOCKS_SUBCATEGORY,
                new AcpElementTuple(MANAGE_MEMBER_LOCKS_ELEMENT, MANAGE_MEMBER_LOCKS_VIEW)
            );
        acpCategoryFactory.addLastSubcategory(categoryEntity, newSubcategory);

        acpCategoryRepository.save(categoryEntity);
    }

}
