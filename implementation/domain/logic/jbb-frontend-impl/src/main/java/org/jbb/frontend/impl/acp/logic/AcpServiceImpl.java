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

import com.google.common.collect.ListMultimap;

import org.jbb.frontend.api.model.AcpCategory;
import org.jbb.frontend.api.model.AcpElement;
import org.jbb.frontend.api.model.AcpSubcategory;
import org.jbb.frontend.api.model.UcpElement;
import org.jbb.frontend.api.service.AcpService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcpServiceImpl implements AcpService {
    @Override
    public List<AcpCategory> selectAllCategoriesOrdered() {
        //TODO
        return null;
    }

    @Override
    public ListMultimap<AcpSubcategory, AcpElement> selectAllSubcategoriesAndElements(String categoryViewName) {
        //TODO
        return null;
    }

    @Override
    public AcpCategory selectCategory(String categoryViewName) {
        //TODO
        return null;
    }

    @Override
    public UcpElement selectElement(String categoryViewName, String subcategoryViewName, String elementViewName) {
        //TODO
        return null;
    }
}
