/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.acp;

import org.jbb.frontend.api.acp.AcpService;
import org.jbb.frontend.api.acp.AcpStructure;
import org.jbb.frontend.impl.acp.dao.AcpCategoryRepository;
import org.jbb.frontend.impl.acp.model.AcpCategoryEntity;
import org.jbb.frontend.impl.acp.model.AcpElementEntity;
import org.jbb.frontend.impl.acp.model.AcpSubcategoryEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;

import javax.cache.annotation.CacheResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultAcpService implements AcpService {

    private final AcpCategoryRepository categoryRepository;

    @Override
    @CacheResult(cacheName = AcpCaches.ACP_STRUCTURE)
    public AcpStructure getAcpStructure() {
        AcpStructure.Builder builder = new AcpStructure.Builder();
        new ArrayList<>(categoryRepository.findByOrderByOrdering())
                .forEach(categoryEntity -> builder.add(mapToModel(categoryEntity)));
        return builder.build();
    }

    private AcpStructure.Category mapToModel(AcpCategoryEntity categoryEntity) {
        AcpStructure.Category.Builder builder = new AcpStructure.Category.Builder();
        builder.name(categoryEntity.getName())
                .viewName(categoryEntity.getViewName());
        categoryEntity.getSubcategories().sort((Comparator.comparing(AcpSubcategoryEntity::getOrdering)));
        categoryEntity.getSubcategories()
                .forEach(subCategoryEntity -> builder.add(mapToModel(subCategoryEntity)));
        return builder.build();
    }

    private AcpStructure.SubCategory mapToModel(AcpSubcategoryEntity subCategoryEntity) {
        AcpStructure.SubCategory.Builder builder = new AcpStructure.SubCategory.Builder();
        builder.name(subCategoryEntity.getName());
        subCategoryEntity.getElements().sort(Comparator.comparing(AcpElementEntity::getOrdering));
        subCategoryEntity.getElements().forEach(elementEntity -> builder.add(mapToModel(elementEntity)));
        return builder.build();
    }

    private AcpStructure.Element mapToModel(AcpElementEntity elementEntity) {
        return AcpStructure.Element.of(elementEntity.getName(), elementEntity.getViewName());
    }

}
