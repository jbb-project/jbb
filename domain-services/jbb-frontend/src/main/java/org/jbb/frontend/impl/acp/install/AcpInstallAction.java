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

import org.jbb.frontend.impl.acp.AcpCategoryFactory;
import org.jbb.frontend.impl.acp.AcpCategoryFactory.AcpCategoryTuple;
import org.jbb.frontend.impl.acp.AcpSubcategoryFactory;
import org.jbb.frontend.impl.acp.AcpSubcategoryFactory.AcpElementTuple;
import org.jbb.frontend.impl.acp.dao.AcpCategoryRepository;
import org.jbb.frontend.impl.acp.model.AcpCategoryEntity;
import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.install.JbbVersions;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import static org.jbb.frontend.impl.acp.AcpConstants.BOARD_CONFIGURATION_SUBCATEGORY;
import static org.jbb.frontend.impl.acp.AcpConstants.BOARD_SETTINGS_ELEMENT;
import static org.jbb.frontend.impl.acp.AcpConstants.BOARD_SETTINGS_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.CREATE_MEMBERS_ELEMENT;
import static org.jbb.frontend.impl.acp.AcpConstants.CREATE_MEMBERS_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.DATABASE_SETTINGS_ELEMENT;
import static org.jbb.frontend.impl.acp.AcpConstants.DATABASE_SETTINGS_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.DATABASE_SUBCATEGORY;
import static org.jbb.frontend.impl.acp.AcpConstants.GENERAL_CATEGORY;
import static org.jbb.frontend.impl.acp.AcpConstants.GENERAL_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.LOGGING_SETTINGS_ELEMENT;
import static org.jbb.frontend.impl.acp.AcpConstants.LOGGING_SETTINGS_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.MAINTENANCE_SUBCATEGORY;
import static org.jbb.frontend.impl.acp.AcpConstants.MANAGE_MEMBERS_ELEMENT;
import static org.jbb.frontend.impl.acp.AcpConstants.MANAGE_MEMBERS_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.MEMBERS_CATEGORY;
import static org.jbb.frontend.impl.acp.AcpConstants.MEMBERS_SUBCATEGORY;
import static org.jbb.frontend.impl.acp.AcpConstants.MEMBERS_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.MONITORING_ELEMENT;
import static org.jbb.frontend.impl.acp.AcpConstants.MONITORING_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.REGISTRATION_SETTINGS_ELEMENT;
import static org.jbb.frontend.impl.acp.AcpConstants.REGISTRATION_SETTINGS_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.SERVER_CONFIGURATION_SUBCATEGORY;
import static org.jbb.frontend.impl.acp.AcpConstants.SYSTEM_CATEGORY;
import static org.jbb.frontend.impl.acp.AcpConstants.SYSTEM_VIEW;

@Component
@RequiredArgsConstructor
public class AcpInstallAction implements InstallUpdateAction {

    private final AcpCategoryFactory acpCategoryFactory;
    private final AcpSubcategoryFactory acpSubcategoryFactory;

    private final AcpCategoryRepository acpCategoryRepository;

    @Override
    public Version fromVersion() {
        return JbbVersions.VERSION_0_6_0;
    }

    @Override
    public void install(InstallationData installationData) {

        AcpCategoryEntity generalCategory = acpCategoryFactory.createWithSubcategories(
                new AcpCategoryTuple(GENERAL_CATEGORY, GENERAL_VIEW),
                acpSubcategoryFactory.createWithElements(BOARD_CONFIGURATION_SUBCATEGORY,
                        new AcpElementTuple(BOARD_SETTINGS_ELEMENT, BOARD_SETTINGS_VIEW),
                        new AcpElementTuple(REGISTRATION_SETTINGS_ELEMENT, REGISTRATION_SETTINGS_VIEW)
                ),
                acpSubcategoryFactory.createWithElements(SERVER_CONFIGURATION_SUBCATEGORY,
                        new AcpElementTuple(LOGGING_SETTINGS_ELEMENT, LOGGING_SETTINGS_VIEW)
                )
        );

        AcpCategoryEntity membersCategory = acpCategoryFactory.createWithSubcategories(
                new AcpCategoryTuple(MEMBERS_CATEGORY, MEMBERS_VIEW),
                acpSubcategoryFactory.createWithElements(MEMBERS_SUBCATEGORY,
                        new AcpElementTuple(MANAGE_MEMBERS_ELEMENT, MANAGE_MEMBERS_VIEW),
                        new AcpElementTuple(CREATE_MEMBERS_ELEMENT, CREATE_MEMBERS_VIEW)
                )
        );

        AcpCategoryEntity systemCategory = acpCategoryFactory.createWithSubcategories(
                new AcpCategoryTuple(SYSTEM_CATEGORY, SYSTEM_VIEW),
                acpSubcategoryFactory.createWithElements(DATABASE_SUBCATEGORY,
                        new AcpElementTuple(DATABASE_SETTINGS_ELEMENT, DATABASE_SETTINGS_VIEW)
                ),
                acpSubcategoryFactory.createWithElements(MAINTENANCE_SUBCATEGORY,
                        new AcpElementTuple(MONITORING_ELEMENT, MONITORING_VIEW)
                )
        );

        acpCategoryRepository.save(generalCategory);
        acpCategoryRepository.save(membersCategory);
        acpCategoryRepository.save(systemCategory);

    }

}
