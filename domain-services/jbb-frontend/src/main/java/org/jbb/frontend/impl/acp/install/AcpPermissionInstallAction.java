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

import static org.jbb.frontend.impl.acp.AcpConstants.EFFECTIVE_PERMISSIONS_ADMINISTRATORS_ELEMENT;
import static org.jbb.frontend.impl.acp.AcpConstants.EFFECTIVE_PERMISSIONS_ADMINISTRATORS_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.EFFECTIVE_PERMISSIONS_MEMBERS_ELEMENT;
import static org.jbb.frontend.impl.acp.AcpConstants.EFFECTIVE_PERMISSIONS_MEMBERS_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.EFFECTIVE_PERMISSIONS_SUBCATEGORY;
import static org.jbb.frontend.impl.acp.AcpConstants.GLOBAL_PERMISSIONS_ADMINISTRATORS_ELEMENT;
import static org.jbb.frontend.impl.acp.AcpConstants.GLOBAL_PERMISSIONS_ADMINISTRATORS_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.GLOBAL_PERMISSIONS_MEMBERS_ELEMENT;
import static org.jbb.frontend.impl.acp.AcpConstants.GLOBAL_PERMISSIONS_MEMBERS_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.GLOBAL_PERMISSIONS_SUBCATEGORY;
import static org.jbb.frontend.impl.acp.AcpConstants.PERMISSIONS_CATEGORY;
import static org.jbb.frontend.impl.acp.AcpConstants.PERMISSIONS_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.PERMISSION_ROLES_SUBCATEGORY;
import static org.jbb.frontend.impl.acp.AcpConstants.PERMISSION_ROLE_ADMINISTRATORS_ELEMENT;
import static org.jbb.frontend.impl.acp.AcpConstants.PERMISSION_ROLE_ADMINISTRATORS_VIEW;
import static org.jbb.frontend.impl.acp.AcpConstants.PERMISSION_ROLE_MEMBERS_ELEMENT;
import static org.jbb.frontend.impl.acp.AcpConstants.PERMISSION_ROLE_MEMBERS_VIEW;

import com.github.zafarkhaja.semver.Version;
import com.google.common.collect.Iterables;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

@Component
@RequiredArgsConstructor
public class AcpPermissionInstallAction implements InstallUpdateAction {

    private final AcpCategoryFactory acpCategoryFactory;
    private final AcpSubcategoryFactory acpSubcategoryFactory;

    private final AcpCategoryRepository acpCategoryRepository;

    @Override
    public Version fromVersion() {
        return JbbVersions.VERSION_0_10_0;
    }

    @Override
    public void install(InstallationData installationData) {

        List<AcpCategoryEntity> categories = acpCategoryRepository.findByOrderByOrdering();
        AcpCategoryEntity lastCategory = Iterables.getLast(categories);
        Integer newCategoryPosition = lastCategory.getOrdering();
        lastCategory.setOrdering(newCategoryPosition + 1);
        acpCategoryRepository.save(lastCategory);

        AcpCategoryEntity permissionsCategory = acpCategoryFactory.createWithSubcategories(
            new AcpCategoryTuple(PERMISSIONS_CATEGORY, PERMISSIONS_VIEW),
            acpSubcategoryFactory.createWithElements(GLOBAL_PERMISSIONS_SUBCATEGORY,
                new AcpElementTuple(GLOBAL_PERMISSIONS_MEMBERS_ELEMENT,
                    GLOBAL_PERMISSIONS_MEMBERS_VIEW),
                new AcpElementTuple(GLOBAL_PERMISSIONS_ADMINISTRATORS_ELEMENT,
                    GLOBAL_PERMISSIONS_ADMINISTRATORS_VIEW)
            ),
            acpSubcategoryFactory.createWithElements(PERMISSION_ROLES_SUBCATEGORY,
                new AcpElementTuple(PERMISSION_ROLE_MEMBERS_ELEMENT, PERMISSION_ROLE_MEMBERS_VIEW),
                new AcpElementTuple(PERMISSION_ROLE_ADMINISTRATORS_ELEMENT,
                    PERMISSION_ROLE_ADMINISTRATORS_VIEW)
            ),
            acpSubcategoryFactory.createWithElements(EFFECTIVE_PERMISSIONS_SUBCATEGORY,
                new AcpElementTuple(EFFECTIVE_PERMISSIONS_MEMBERS_ELEMENT,
                    EFFECTIVE_PERMISSIONS_MEMBERS_VIEW),
                new AcpElementTuple(EFFECTIVE_PERMISSIONS_ADMINISTRATORS_ELEMENT,
                    EFFECTIVE_PERMISSIONS_ADMINISTRATORS_VIEW)
            )
        );
        permissionsCategory.setOrdering(newCategoryPosition);

        acpCategoryRepository.save(permissionsCategory);

    }

}
