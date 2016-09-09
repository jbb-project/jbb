/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.api.service;

import org.jbb.frontend.api.model.UcpCategory;
import org.jbb.frontend.api.model.UcpElement;

import java.util.List;

public interface UcpService {
    List<UcpCategory> selectAllCategoriesOrdered();

    List<UcpElement> selectAllElementsOrderedForCategoryViewName(String categoryName);

    UcpCategory selectForViewName(String viewName);
}
