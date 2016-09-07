/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp.logic;

import com.google.common.collect.Lists;

import org.jbb.frontend.impl.ucp.dao.UcpCategoryRepository;
import org.jbb.frontend.impl.ucp.dao.UcpElementRepository;
import org.jbb.frontend.impl.ucp.model.UcpCategoryEntity;
import org.jbb.frontend.impl.ucp.model.UcpElementEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component//TODO refactor
public class UcpComponentsAutoCreator {
    private final UcpCategoryRepository categoryRepository;
    private final UcpElementRepository elementRepository;

    @Autowired
    public UcpComponentsAutoCreator(UcpCategoryRepository categoryRepository, UcpElementRepository elementRepository) {
        this.categoryRepository = categoryRepository;
        this.elementRepository = elementRepository;
    }

    @PostConstruct
    @Transactional
    public void buildUcp() {
        if (categoryRepository.count() == 0 && elementRepository.count() == 0) {
            UcpCategoryEntity overviewCategory = UcpCategoryEntity.builder()
                    .name("Overview")
                    .viewName("overview")
                    .ordering(1)
                    .elements(Lists.newArrayList())
                    .build();

            UcpElementEntity statisticsElement = UcpElementEntity.builder()
                    .name("Statistics")
                    .viewName("statistics")
                    .ordering(1)
                    .category(overviewCategory)
                    .build();

            overviewCategory.getElements().add(statisticsElement);

            UcpCategoryEntity profileCategory = UcpCategoryEntity.builder()
                    .name("Profile")
                    .viewName("profile")
                    .ordering(2)
                    .elements(Lists.newArrayList())
                    .build();

            UcpElementEntity editProfileElement = UcpElementEntity.builder()
                    .name("Edit profile")
                    .viewName("editProfile")
                    .ordering(1)
                    .category(profileCategory)
                    .build();

            UcpElementEntity editAccountSettingsElement = UcpElementEntity.builder()
                    .name("Edit account settings")
                    .viewName("editAccount")
                    .ordering(2)
                    .category(profileCategory)
                    .build();

            profileCategory.getElements().add(editProfileElement);
            profileCategory.getElements().add(editAccountSettingsElement);

            categoryRepository.save(overviewCategory);
            categoryRepository.save(profileCategory);
        }
    }

}
