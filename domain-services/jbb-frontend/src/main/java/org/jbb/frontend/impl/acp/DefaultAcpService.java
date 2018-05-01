/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.acp;

import org.jbb.frontend.api.acp.AcpCategory;
import org.jbb.frontend.api.acp.AcpElement;
import org.jbb.frontend.api.acp.AcpService;
import org.jbb.frontend.api.acp.AcpSubcategory;
import org.jbb.frontend.impl.acp.dao.AcpCategoryRepository;
import org.jbb.frontend.impl.acp.dao.AcpElementRepository;
import org.jbb.frontend.impl.acp.dao.AcpSubcategoryRepository;
import org.jbb.frontend.impl.acp.model.AcpElementEntity;
import org.jbb.frontend.impl.acp.model.AcpSubcategoryEntity;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.cache.annotation.CacheResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultAcpService implements AcpService {
    private final AcpCategoryRepository categoryRepository;
    private final AcpSubcategoryRepository subcategoryRepository;
    private final AcpElementRepository elementRepository;

    @Override
    @CacheResult(cacheName = AcpCaches.ACP_CATEGORIES_EAGER)
    public List<AcpCategory> selectAllCategoriesOrdered() {
        return categoryRepository.findByOrderByOrdering().stream()
                .map(category -> (AcpCategory) category)
                .collect(Collectors.toList());
    }

    @Override
    @CacheResult(cacheName = AcpCaches.ACP_SUBCATEGORIES_MAP)
    public NavigableMap<AcpSubcategory, Collection<AcpElement>> selectAllSubcategoriesAndElements(
            String categoryViewName) {
        List<AcpSubcategoryEntity> subcategories = subcategoryRepository.findByCategoryOrderByOrdering(categoryViewName);

        NavigableMap<AcpSubcategory, Collection<AcpElement>> result = new TreeMap<>(
                (Comparator<AcpSubcategory> & Serializable)
                        (o1, o2) -> o1.getOrdering().compareTo(o2.getOrdering())
        );

        for (AcpSubcategoryEntity subcategory : subcategories) {
            List<AcpElement> orderedElements = subcategory.getElements().stream()
                    .sorted(Comparator.comparing(AcpElementEntity::getOrdering))
                    .map(AcpElement.class::cast)
                    .collect(Collectors.toList());
            result.put(subcategory, orderedElements);
        }

        return result;
    }

    @Override
    @CacheResult(cacheName = AcpCaches.ACP_CATEGORIES)
    public AcpCategory selectCategory(String categoryViewName) {
        return categoryRepository.findByViewName(categoryViewName);
    }

    @Override
    @CacheResult(cacheName = AcpCaches.ACP_ELEMENTS)
    public AcpElement selectElement(String categoryViewName, String elementViewName) {
        return elementRepository.findByCategoryAndElementName(categoryViewName, elementViewName);
    }
}
