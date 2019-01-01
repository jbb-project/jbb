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

import com.google.common.collect.Lists;

import org.jbb.frontend.impl.acp.model.AcpCategoryEntity;
import org.jbb.frontend.impl.acp.model.AcpSubcategoryEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Component
public class AcpCategoryFactory {
    private int categoryCounter = 1;

    public AcpCategoryEntity createWithSubcategories(AcpCategoryTuple categoryTuple,
                                                     AcpSubcategoryEntity... subcategoryEntities) {
        AcpCategoryEntity category = AcpCategoryEntity.builder()
                .name(categoryTuple.getName())
                .viewName(categoryTuple.getViewName())
                .ordering(categoryCounter++)
                .subcategories(Lists.newArrayList(Arrays.asList(subcategoryEntities)))
                .build();

        int subcategoryCounter = 1;
        for (AcpSubcategoryEntity subcategory : category.getSubcategories()) {
            subcategory.setOrdering(subcategoryCounter++);
            subcategory.setCategory(category);
        }

        return category;
    }

    @Getter
    @AllArgsConstructor
    public static class AcpCategoryTuple {
        private String name;
        private String viewName;
    }
}
