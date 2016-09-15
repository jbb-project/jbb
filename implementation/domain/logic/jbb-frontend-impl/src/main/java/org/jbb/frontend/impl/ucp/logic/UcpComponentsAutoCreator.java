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

import org.jbb.frontend.impl.ucp.dao.UcpCategoryRepository;
import org.jbb.frontend.impl.ucp.dao.UcpElementRepository;
import org.jbb.frontend.impl.ucp.logic.UcpCategoryFactory.UcpCategoryTuple;
import org.jbb.frontend.impl.ucp.logic.UcpCategoryFactory.UcpElementTuple;
import org.jbb.frontend.impl.ucp.model.UcpCategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
public class UcpComponentsAutoCreator {
    private final UcpCategoryFactory ucpCategoryFactory;
    private final UcpCategoryRepository categoryRepository;
    private final UcpElementRepository elementRepository;

    @Autowired
    public UcpComponentsAutoCreator(UcpCategoryFactory ucpCategoryFactory,
                                    UcpCategoryRepository categoryRepository,
                                    UcpElementRepository elementRepository) {
        this.ucpCategoryFactory = ucpCategoryFactory;
        this.categoryRepository = categoryRepository;
        this.elementRepository = elementRepository;
    }

    @PostConstruct
    @Transactional
    public void buildUcp() {
        if (ucpIsEmpty()) {

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
    }

    private boolean ucpIsEmpty() {
        return categoryRepository.count() == 0 && elementRepository.count() == 0;
    }

}
