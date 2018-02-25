/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp;

import org.jbb.frontend.api.ucp.UcpCategory;
import org.jbb.frontend.api.ucp.UcpElement;
import org.jbb.frontend.api.ucp.UcpService;
import org.jbb.frontend.impl.ucp.dao.UcpCategoryRepository;
import org.jbb.frontend.impl.ucp.dao.UcpElementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import javax.cache.annotation.CacheResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultUcpService implements UcpService {
    private final UcpCategoryRepository categoryRepository;
    private final UcpElementRepository elementRepository;

    @Override
    @CacheResult(cacheName = UcpCaches.UCP_EAGER)
    public List<UcpCategory> selectAllCategoriesOrdered() {
        return categoryRepository.findByOrderByOrdering().stream()
                .map(category -> (UcpCategory) category)
                .collect(Collectors.toList());
    }

    @Override
    @CacheResult(cacheName = UcpCaches.UCP_ELEMENTS_LIST)
    public List<UcpElement> selectAllElementsOrderedForCategoryViewName(String categoryViewName) {
        return elementRepository.findByCategoryNameOrderByOrdering(categoryViewName).stream()
                .map(element -> (UcpElement) element)
                .collect(Collectors.toList());
    }

    @Override
    @CacheResult(cacheName = UcpCaches.UCP_CATEGORY)
    public UcpCategory selectCategoryForViewName(String categoryViewName) {
        return categoryRepository.findByViewName(categoryViewName);
    }

    @Override
    @CacheResult(cacheName = UcpCaches.UCP_ELEMENT)
    public UcpElement selectElementForViewName(String categoryViewName, String elementViewName) {
        return elementRepository.findByCategoryAndElementName(categoryViewName, elementViewName);
    }
}
