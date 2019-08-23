/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.ucp.controller;

import com.google.common.collect.Iterables;

import org.jbb.frontend.api.ucp.UcpService;
import org.jbb.frontend.api.ucp.UcpStructure;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UcpController {
    private final UcpService ucpService;

    @RequestMapping("/ucp")
    public String ucpMain() {
        UcpStructure ucpStructure = ucpService.getUcpStructure();
        return "redirect:/ucp/" + Iterables.get(ucpStructure.getCategories(), 0).getViewName();
    }

    @RequestMapping("/ucp/{categoryViewName}")
    public String category(@PathVariable("categoryViewName") String categoryViewName) {
        UcpStructure ucpStructure = ucpService.getUcpStructure();
        UcpStructure.Category category = ucpStructure.findCategoryByViewName(categoryViewName);
        List<UcpStructure.Element> ucpElements = category.getElements();
        return "redirect:/ucp/" + categoryViewName + "/" + Iterables.get(ucpElements, 0)
            .getViewName();
    }

}
