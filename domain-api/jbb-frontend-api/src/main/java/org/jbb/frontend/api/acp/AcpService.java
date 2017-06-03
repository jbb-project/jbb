/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.api.acp;

import com.google.common.collect.TreeMultimap;

import java.util.List;

public interface AcpService {
    List<AcpCategory> selectAllCategoriesOrdered();

    TreeMultimap<AcpSubcategory, AcpElement> selectAllSubcategoriesAndElements(String categoryViewName);

    AcpCategory selectCategory(String categoryViewName);

    AcpElement selectElement(String categoryViewName, String elementViewName);
}
