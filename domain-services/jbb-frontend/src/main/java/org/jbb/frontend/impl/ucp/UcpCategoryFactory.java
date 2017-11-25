/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jbb.frontend.impl.ucp.model.UcpCategoryEntity;
import org.jbb.frontend.impl.ucp.model.UcpElementEntity;
import org.springframework.stereotype.Component;

@Component
public class UcpCategoryFactory {
    private int categoryCounter = 1;

    public UcpCategoryEntity createWithElements(UcpCategoryTuple categoryTuple,
                                                UcpElementTuple... elementTuples) {
        UcpCategoryEntity category = UcpCategoryEntity.builder()
                .name(categoryTuple.getName())
                .viewName(categoryTuple.getViewName())
                .ordering(categoryCounter++)
                .elements(Lists.newArrayList())
                .build();

        int elementCounter = 1;

        for (UcpElementTuple elementTuple : elementTuples) {
            UcpElementEntity element = UcpElementEntity.builder()
                    .name(elementTuple.getName())
                    .viewName(elementTuple.getViewName())
                    .ordering(elementCounter++)
                    .category(category)
                    .build();
            category.getElements().add(element);
        }

        return category;
    }

    @Getter
    @AllArgsConstructor
    public static class UcpCategoryTuple {
        private String name;
        private String viewName;
    }

    @Getter
    @AllArgsConstructor
    public static class UcpElementTuple {
        private String name;
        private String viewName;
    }
}
