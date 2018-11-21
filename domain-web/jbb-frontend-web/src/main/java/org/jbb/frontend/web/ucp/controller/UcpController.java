/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.ucp.controller;

import com.google.common.collect.Iterables;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jbb.frontend.api.ucp.UcpCategory;
import org.jbb.frontend.api.ucp.UcpElement;
import org.jbb.frontend.api.ucp.UcpService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class UcpController {
    private final UcpService ucpService;

    @RequestMapping("/ucp")
    public String ucpMain() {
        List<UcpCategory> ucpCategories = ucpService.selectAllCategoriesOrdered();
        return "redirect:/ucp/" + Iterables.get(ucpCategories, 0).getViewName();
    }

    @RequestMapping("/ucp/{categoryViewName}")
    public String category(@PathVariable("categoryViewName") String categoryViewName) {
        List<UcpElement> ucpElements = ucpService.selectAllElementsOrderedForCategoryViewName(categoryViewName);
        return "redirect:/ucp/" + categoryViewName + "/" + Iterables.get(ucpElements, 0)
            .getViewName();
    }

}
