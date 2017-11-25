/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp.install;

import static org.jbb.frontend.impl.ucp.UcpConstants.EDIT_ACCOUNT_ELEMENT;
import static org.jbb.frontend.impl.ucp.UcpConstants.EDIT_ACCOUNT_VIEW;
import static org.jbb.frontend.impl.ucp.UcpConstants.EDIT_PROFILE_ELEMENT;
import static org.jbb.frontend.impl.ucp.UcpConstants.EDIT_PROFILE_VIEW;
import static org.jbb.frontend.impl.ucp.UcpConstants.OVERVIEW_CATEGORY;
import static org.jbb.frontend.impl.ucp.UcpConstants.OVERVIEW_VIEW;
import static org.jbb.frontend.impl.ucp.UcpConstants.PROFILE_CATEGORY;
import static org.jbb.frontend.impl.ucp.UcpConstants.PROFILE_VIEW;
import static org.jbb.frontend.impl.ucp.UcpConstants.STATISTICS_ELEMENT;
import static org.jbb.frontend.impl.ucp.UcpConstants.STATISTICS_VIEW;

import com.github.zafarkhaja.semver.Version;
import lombok.RequiredArgsConstructor;
import org.jbb.frontend.impl.ucp.UcpCategoryFactory;
import org.jbb.frontend.impl.ucp.UcpCategoryFactory.UcpCategoryTuple;
import org.jbb.frontend.impl.ucp.UcpCategoryFactory.UcpElementTuple;
import org.jbb.frontend.impl.ucp.dao.UcpCategoryRepository;
import org.jbb.frontend.impl.ucp.model.UcpCategoryEntity;
import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.install.JbbVersions;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UcpInstallAction implements InstallUpdateAction {

    private final UcpCategoryFactory ucpCategoryFactory;

    private final UcpCategoryRepository categoryRepository;

    @Override
    public Version fromVersion() {
        return JbbVersions.VERSION_0_5_0;
    }

    @Override
    public void install(InstallationData installationData) {

        UcpCategoryEntity overviewCategory = ucpCategoryFactory.createWithElements(
            new UcpCategoryTuple(OVERVIEW_CATEGORY, OVERVIEW_VIEW),
            new UcpElementTuple(STATISTICS_ELEMENT, STATISTICS_VIEW)
        );

        UcpCategoryEntity profileCategory = ucpCategoryFactory.createWithElements(
            new UcpCategoryTuple(PROFILE_CATEGORY, PROFILE_VIEW),
            new UcpElementTuple(EDIT_PROFILE_ELEMENT, EDIT_PROFILE_VIEW),
            new UcpElementTuple(EDIT_ACCOUNT_ELEMENT, EDIT_ACCOUNT_VIEW)
        );

        categoryRepository.save(overviewCategory);
        categoryRepository.save(profileCategory);
    }

}
