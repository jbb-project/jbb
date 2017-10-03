/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp.logic;

import lombok.RequiredArgsConstructor;
import org.jbb.frontend.impl.ucp.dao.UcpCategoryRepository;
import org.jbb.frontend.impl.ucp.dao.UcpElementRepository;
import org.jbb.frontend.impl.ucp.logic.UcpCategoryFactory.UcpCategoryTuple;
import org.jbb.frontend.impl.ucp.logic.UcpCategoryFactory.UcpElementTuple;
import org.jbb.frontend.impl.ucp.model.UcpCategoryEntity;
import org.jbb.install.InstallAction;
import org.jbb.install.InstallationData;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UcpInstallAction implements InstallAction {

    private final UcpCategoryFactory ucpCategoryFactory;
    private final UcpCategoryRepository categoryRepository;
    private final UcpElementRepository elementRepository;

    @Override
    public void install(InstallationData installationData) {
        if (ucpIsNotEmpty()) {
            return;
        }

        UcpCategoryEntity overviewCategory = ucpCategoryFactory.createWithElements(
            new UcpCategoryTuple("Overview", "overview"),
            new UcpElementTuple("Statistics", "statistics")
        );

        UcpCategoryEntity profileCategory = ucpCategoryFactory.createWithElements(
            new UcpCategoryTuple("Profile", "profile"),
            new UcpElementTuple("Edit profile", "edit"),
            new UcpElementTuple("Edit account settings", "editAccount")
        );

        categoryRepository.save(overviewCategory);
        categoryRepository.save(profileCategory);
    }

    private boolean ucpIsNotEmpty() {
        return categoryRepository.count() > 0 || elementRepository.count() > 0;
    }
}
