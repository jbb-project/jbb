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

import org.jbb.frontend.impl.acp.model.AcpElementEntity;
import org.jbb.frontend.impl.acp.model.AcpSubcategoryEntity;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Component
public class AcpSubcategoryFactory {

    public AcpSubcategoryEntity createWithElements(String subcategoryName,
                                                   AcpElementTuple... elementTuples) {
        AcpSubcategoryEntity subcategory = AcpSubcategoryEntity.builder()
                .name(subcategoryName)
                .elements(Lists.newArrayList())
                .build();

        int elementCounter = 1;

        for (AcpSubcategoryFactory.AcpElementTuple elementTuple : elementTuples) {
            AcpElementEntity element = AcpElementEntity.builder()
                    .name(elementTuple.getName())
                    .viewName(elementTuple.getViewName())
                    .ordering(elementCounter++)
                    .subcategory(subcategory)
                    .build();
            subcategory.getElements().add(element);
        }

        return subcategory;
    }

    @Getter
    @AllArgsConstructor
    public static class AcpElementTuple {
        private String name;
        private String viewName;
    }
}
