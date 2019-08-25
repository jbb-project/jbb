/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp;

import org.jbb.frontend.api.ucp.UcpService;
import org.jbb.frontend.api.ucp.UcpStructure;
import org.jbb.frontend.impl.ucp.dao.UcpCategoryRepository;
import org.jbb.frontend.impl.ucp.model.UcpCategoryEntity;
import org.jbb.frontend.impl.ucp.model.UcpElementEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;

import javax.cache.annotation.CacheResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultUcpService implements UcpService {

    private final UcpCategoryRepository categoryRepository;

    @Override
    @CacheResult(cacheName = UcpCaches.UCP_STRUCTURE)
    public UcpStructure getUcpStructure() {
        UcpStructure.Builder builder = new UcpStructure.Builder();
        new ArrayList<>(categoryRepository.findByOrderByOrdering())
                .forEach(categoryEntity -> builder.add(mapToModel(categoryEntity)));
        return builder.build();
    }

    private UcpStructure.Category mapToModel(UcpCategoryEntity categoryEntity) {
        UcpStructure.Category.Builder builder = new UcpStructure.Category.Builder();
        builder.name(categoryEntity.getName())
                .viewName(categoryEntity.getViewName());
        categoryEntity.getElements().sort((Comparator.comparing(UcpElementEntity::getOrdering)));
        categoryEntity.getElements()
                .forEach(elementEntity -> builder.add(mapToModel(elementEntity)));
        return builder.build();
    }

    private UcpStructure.Element mapToModel(UcpElementEntity elementEntity) {
        return UcpStructure.Element.of(elementEntity.getName(), elementEntity.getViewName());
    }

}
