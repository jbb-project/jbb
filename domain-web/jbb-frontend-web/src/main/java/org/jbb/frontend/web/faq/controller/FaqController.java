/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.faq.controller;

import lombok.RequiredArgsConstructor;
import org.jbb.frontend.api.faq.FaqService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class FaqController {
    private static final String FAQ_VIEW_NAME = "faq";

    private final FaqService faqService;

    @RequestMapping("/faq")
    public String getFaq(Model model) {
        return FAQ_VIEW_NAME; //NOSONAR
    }


}
