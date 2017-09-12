/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.acp.logic;

import lombok.RequiredArgsConstructor;
import org.jbb.frontend.impl.acp.dao.AcpCategoryRepository;
import org.jbb.frontend.impl.acp.dao.AcpElementRepository;
import org.jbb.frontend.impl.acp.dao.AcpSubcategoryRepository;
import org.jbb.frontend.impl.acp.logic.AcpCategoryFactory.AcpCategoryTuple;
import org.jbb.frontend.impl.acp.logic.AcpSubcategoryFactory.AcpElementTuple;
import org.jbb.frontend.impl.acp.model.AcpCategoryEntity;
import org.jbb.install.InstallAction;
import org.jbb.install.InstallationData;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AcpInstallAction implements InstallAction {

    private final AcpCategoryFactory acpCategoryFactory;
    private final AcpSubcategoryFactory acpSubcategoryFactory;

    private final AcpCategoryRepository acpCategoryRepository;
    private final AcpSubcategoryRepository acpSubcategoryRepository;
    private final AcpElementRepository acpElementRepository;

    @Override
    public void install(InstallationData installationData) {
        if (acpIsNotEmpty()) {
            return;
        }

        AcpCategoryEntity generalCategory = acpCategoryFactory.createWithSubcategories(
            new AcpCategoryTuple("General", "general"),
            acpSubcategoryFactory.createWithElements(
                "Board configuration",
                new AcpElementTuple("Board settings", "board"),
                new AcpElementTuple("Member registration settings", "registration"),
                new AcpElementTuple("Member lockout settings", "lockout"),
                new AcpElementTuple("Forum management", "forums")
            ),
            acpSubcategoryFactory.createWithElements(
                "Server configuration",
                new AcpElementTuple("Logging & debugging settings", "logging"),
                new AcpElementTuple("Cache settings", "cache")
            )
        );

        AcpCategoryEntity membersCategory = acpCategoryFactory.createWithSubcategories(
            new AcpCategoryTuple("Members and groups", "members"),
            acpSubcategoryFactory.createWithElements(
                "Members",
                new AcpElementTuple("Search & manage members", "manage"),
                new AcpElementTuple("Create new member", "create")
            )
        );

        AcpCategoryEntity systemCategory = acpCategoryFactory.createWithSubcategories(
            new AcpCategoryTuple("System", "system"),
            acpSubcategoryFactory.createWithElements(
                "Sessions",
                new AcpElementTuple("Sessions management", "sessions")
            ),
            acpSubcategoryFactory.createWithElements(
                "Database",
                new AcpElementTuple("Database settings", "database")
            ),
            acpSubcategoryFactory.createWithElements(
                "Maintenance",
                new AcpElementTuple("Monitoring", "monitoring")
            )
        );

        acpCategoryRepository.save(generalCategory);
        acpCategoryRepository.save(membersCategory);
        acpCategoryRepository.save(systemCategory);

    }

    private boolean acpIsNotEmpty() {
        return acpCategoryRepository.count() > 0 ||
            acpSubcategoryRepository.count() > 0 ||
            acpElementRepository.count() > 0;
    }
}
