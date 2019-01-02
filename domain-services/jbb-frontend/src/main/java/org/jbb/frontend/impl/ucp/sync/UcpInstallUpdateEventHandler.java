/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp.sync;

import com.google.common.eventbus.Subscribe;

import org.jbb.frontend.api.ucp.UcpStructure;
import org.jbb.frontend.impl.ucp.UcpCategoryFactory;
import org.jbb.frontend.impl.ucp.UcpStructureProvider;
import org.jbb.frontend.impl.ucp.dao.UcpCategoryRepository;
import org.jbb.frontend.impl.ucp.model.UcpCategoryEntity;
import org.jbb.lib.eventbus.JbbEventBusListener;
import org.jbb.system.event.InstallUpgradePerformedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UcpInstallUpdateEventHandler implements JbbEventBusListener {

    private final UcpStructureProvider ucpStructureProvider;

    private final UcpCategoryFactory ucpCategoryFactory;

    private final UcpCategoryRepository categoryRepository;

    @Subscribe
    public void sync(InstallUpgradePerformedEvent event) {
        sync();
    }

    @Transactional
    public void sync() {
        // clean up all first
        categoryRepository.findAll().forEach(categoryRepository::delete);

        // insert a new UCP
        ucpStructureProvider.getUcpStructure().getCategories().forEach(this::createAndSaveCategory);
    }

    private void createAndSaveCategory(UcpStructure.Category category) {
        UcpCategoryEntity categoryEntity = ucpCategoryFactory.createWithElements(new UcpCategoryFactory.UcpCategoryTuple(category.getName(), category.getViewName()),
                createElements(category.getElements())
        );
        categoryRepository.save(categoryEntity);
    }

    private UcpCategoryFactory.UcpElementTuple[] createElements(List<UcpStructure.Element> elements) {
        return elements.stream().map(this::createElement)
                .collect(Collectors.toList()).toArray(new UcpCategoryFactory.UcpElementTuple[]{});
    }

    private UcpCategoryFactory.UcpElementTuple createElement(UcpStructure.Element element) {
        return new UcpCategoryFactory.UcpElementTuple(element.getName(), element.getViewName());
    }
}
