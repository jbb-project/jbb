/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.ucp;

import com.google.common.collect.Iterables;

import org.jbb.frontend.api.model.UcpCategory;
import org.jbb.frontend.api.model.UcpElement;
import org.jbb.frontend.api.service.UcpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class UcpController {
    @Autowired
    private UcpService ucpService;

    @RequestMapping("/ucp")
    public String faq() {
        List<UcpCategory> ucpCategories = ucpService.selectAllCategoriesOrdered();
        return "redirect:/ucp/" + Iterables.getFirst(ucpCategories, null).getViewName();
    }

    @RequestMapping("/ucp/{categoryViewName}")
    public String category(@PathVariable("categoryViewName") String categoryViewName, Model model) {
        List<UcpCategory> ucpCategories = ucpService.selectAllCategoriesOrdered();
        model.addAttribute("ucpCategories", ucpCategories);

        List<UcpElement> ucpElements = ucpService.selectAllElementsOrderedForCategoryViewName(categoryViewName);
        model.addAttribute("ucpElements", ucpElements);

        model.addAttribute("currentCategory", ucpService.selectForViewName(categoryViewName));
        return "ucpLayout";
    }

}
