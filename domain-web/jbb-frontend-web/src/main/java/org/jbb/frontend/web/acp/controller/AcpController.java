/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.acp.controller;

import com.google.common.collect.Iterables;

import org.jbb.frontend.api.acp.AcpService;
import org.jbb.frontend.api.acp.AcpStructure;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AcpController {
    private final AcpService acpService;

    @RequestMapping("/acp")
    public String acpMain() {
        AcpStructure acpStructure = acpService.getAcpStructure();
        return "redirect:/acp/" + Iterables.get(acpStructure.getCategories(), 0).getViewName();
    }
}
