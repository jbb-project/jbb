/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.acp.sync;

import com.google.common.eventbus.Subscribe;

import org.jbb.frontend.impl.acp.AcpCategoryFactory;
import org.jbb.frontend.impl.acp.AcpStructure;
import org.jbb.frontend.impl.acp.AcpSubcategoryFactory;
import org.jbb.frontend.impl.acp.dao.AcpCategoryRepository;
import org.jbb.frontend.impl.acp.model.AcpCategoryEntity;
import org.jbb.frontend.impl.acp.model.AcpSubcategoryEntity;
import org.jbb.lib.eventbus.JbbEventBusListener;
import org.jbb.system.event.InstallUpgradePerformedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AcpInstallUpdateEventHandler implements JbbEventBusListener {

    private final AcpStructure acpStructure = new AcpStructure();

    private final AcpCategoryFactory acpCategoryFactory;
    private final AcpSubcategoryFactory acpSubcategoryFactory;

    private final AcpCategoryRepository acpCategoryRepository;

    @Subscribe
    public void sync(InstallUpgradePerformedEvent event) {
        sync();
    }

    @Transactional
    public void sync() {
        // clean up all first
        acpCategoryRepository.findAll().forEach(acpCategoryRepository::delete);

        // insert a new ACP
        acpStructure.getCategories().forEach(this::createAndSaveCategory);
    }

    private void createAndSaveCategory(AcpStructure.Category category) {
        AcpCategoryEntity categoryEntity = acpCategoryFactory.createWithSubcategories(
                new AcpCategoryFactory.AcpCategoryTuple(category.getName(), category.getViewName()),
                createSubcategories(category.getSubCategories()));
        acpCategoryRepository.save(categoryEntity);
    }

    private AcpSubcategoryEntity[] createSubcategories(List<AcpStructure.SubCategory> subCategories) {
        return subCategories.stream().map(this::createSubCategory)
                .collect(Collectors.toList()).toArray(new AcpSubcategoryEntity[]{});
    }

    private AcpSubcategoryEntity createSubCategory(AcpStructure.SubCategory subCategory) {
        return acpSubcategoryFactory.createWithElements(subCategory.getName(),
                createElements(subCategory.getElements()));
    }

    private AcpSubcategoryFactory.AcpElementTuple[] createElements(List<AcpStructure.Element> elements) {
        return elements.stream().map(this::createElement)
                .collect(Collectors.toList()).toArray(new AcpSubcategoryFactory.AcpElementTuple[]{});
    }

    private AcpSubcategoryFactory.AcpElementTuple createElement(AcpStructure.Element element) {
        return new AcpSubcategoryFactory.AcpElementTuple(element.getName(), element.getViewName());
    }
}
