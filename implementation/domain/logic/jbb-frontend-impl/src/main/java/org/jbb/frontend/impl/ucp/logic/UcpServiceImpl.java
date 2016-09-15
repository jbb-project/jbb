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

import org.jbb.frontend.api.model.UcpCategory;
import org.jbb.frontend.api.model.UcpElement;
import org.jbb.frontend.api.service.UcpService;
import org.jbb.frontend.impl.ucp.dao.UcpCategoryRepository;
import org.jbb.frontend.impl.ucp.dao.UcpElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UcpServiceImpl implements UcpService {
    private final UcpCategoryRepository categoryRepository;
    private final UcpElementRepository elementRepository;

    @Autowired
    public UcpServiceImpl(UcpCategoryRepository categoryRepository,
                          UcpElementRepository elementRepository) {
        this.categoryRepository = categoryRepository;
        this.elementRepository = elementRepository;
    }

    @Override
    public List<UcpCategory> selectAllCategoriesOrdered() {
        return categoryRepository.findByOrderByOrdering().stream()
                .map(category -> (UcpCategory) category)
                .collect(Collectors.toList());
    }

    @Override
    public List<UcpElement> selectAllElementsOrderedForCategoryViewName(String categoryName) {
        return elementRepository.findByCategoryNameOrderByOrdering(categoryName).stream()
                .map(element -> (UcpElement) element)
                .collect(Collectors.toList());
    }

    @Override
    public UcpCategory selectForViewName(String viewName) {
        return categoryRepository.findByViewName(viewName);
    }

    @Override
    public UcpElement selectElementForViewName(String categoryName, String elementName) {
        return elementRepository.findByCategoryAndElementName(categoryName, elementName);
    }
}
