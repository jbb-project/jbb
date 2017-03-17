/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.acp.controller;

import com.google.common.collect.Iterables;

import org.jbb.frontend.api.acp.AcpCategory;
import org.jbb.frontend.api.acp.AcpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class AcpController {
    private final AcpService acpService;

    @Autowired
    public AcpController(AcpService acpService) {
        this.acpService = acpService;
    }

    @RequestMapping("/acp")
    public String acpMain() {
        List<AcpCategory> acpCategories = acpService.selectAllCategoriesOrdered();
        return "redirect:/acp/" + Iterables.getFirst(acpCategories, null).getViewName();
    }
}
