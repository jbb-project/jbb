/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.acp.logic;

import com.google.common.collect.TreeMultimap;

import org.jbb.frontend.api.model.AcpCategory;
import org.jbb.frontend.api.model.AcpElement;
import org.jbb.frontend.api.model.AcpSubcategory;
import org.jbb.frontend.api.service.AcpService;
import org.jbb.frontend.impl.acp.dao.AcpCategoryRepository;
import org.jbb.frontend.impl.acp.dao.AcpElementRepository;
import org.jbb.frontend.impl.acp.dao.AcpSubcategoryRepository;
import org.jbb.frontend.impl.acp.model.AcpSubcategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AcpServiceImpl implements AcpService {
    private final AcpCategoryRepository categoryRepository;
    private final AcpSubcategoryRepository subcategoryRepository;
    private final AcpElementRepository elementRepository;

    @Autowired
    public AcpServiceImpl(AcpCategoryRepository acpCategoryRepository,
                          AcpSubcategoryRepository acpSubcategoryRepository,
                          AcpElementRepository acpElementRepository) {
        this.categoryRepository = acpCategoryRepository;
        this.subcategoryRepository = acpSubcategoryRepository;
        this.elementRepository = acpElementRepository;
    }

    @Override
    public List<AcpCategory> selectAllCategoriesOrdered() {
        return categoryRepository.findByOrderByOrdering().stream()
                .map(category -> (AcpCategory) category)
                .collect(Collectors.toList());
    }

    @Override
    public TreeMultimap<AcpSubcategory, AcpElement> selectAllSubcategoriesAndElements(String categoryViewName) {
        List<AcpSubcategoryEntity> subcategories = subcategoryRepository.findByCategoryOrderByOrdering(categoryViewName);

        TreeMultimap<AcpSubcategory, AcpElement> multimap = TreeMultimap.create(
                (o1, o2) -> o1.getOrdering() - o2.getOrdering(), ((o1, o2) -> o1.getOrdering() - o2.getOrdering())

        );
        for (AcpSubcategoryEntity subcategory : subcategories) {
            multimap.putAll(subcategory, subcategory.getElements());
        }

        return multimap;
    }

    @Override
    public AcpCategory selectCategory(String categoryViewName) {
        return categoryRepository.findByViewName(categoryViewName);
    }

    @Override
    public AcpElement selectElement(String categoryViewName, String elementViewName) {
        return elementRepository.findByCategoryAndElementName(categoryViewName, elementViewName);
    }
}
