/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.acp.faq;


import org.jbb.frontend.api.faq.FaqService;
import org.jbb.frontend.api.faq.model.FaqTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/acp/faq")
public class FaqController {

    private static final String FAQ_VIEW_NAME = "faq";
    private static final String FAQ_FORM_NAME = "faqform";
    private final FaqService acpFaqService;

    @Autowired
    public FaqController(FaqService acpFaqService){
        this.acpFaqService = acpFaqService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getAllSavedFaq(Model model){

        Map<String, List<FaqTuple>> faqDataMap = acpFaqService.getFaqDataMap();

        model.addAttribute(FAQ_FORM_NAME,faqDataMap);
        return FAQ_VIEW_NAME;

    }
}
