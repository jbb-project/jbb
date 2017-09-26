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
import org.jbb.frontend.api.faq.Faq;
import org.jbb.frontend.api.faq.FaqService;
import org.jbb.frontend.web.faq.form.FaqForm;
import org.jbb.frontend.web.faq.logic.FaqTranslator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequiredArgsConstructor
@RequestMapping("/acp/general/faq")
public class AcpFaqSettingsController {

    private static final String VIEW_NAME = "acp/general/faq";
    private static final String FAQ_SETTINGS_FORM = "faqSettingsForm";
    private static final String FORM_SAVED_FLAG = "faqSettingsFormSaved";

    private final FaqService faqService;
    private final FaqTranslator faqTranslator;

    @RequestMapping(method = RequestMethod.GET)
    public String faqSettingsGet(Model model,
        @ModelAttribute(FAQ_SETTINGS_FORM) FaqForm form) {

        Faq faq = faqService.getFaq();
        FaqForm faqForm = faqTranslator.toForm(faq);
        model.addAttribute(FAQ_SETTINGS_FORM, faqForm);

        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String faqSettingsPost(Model model,
        @ModelAttribute(FAQ_SETTINGS_FORM) FaqForm form) {

        Faq faq = faqTranslator.toFaq(form);
        faqService.setFaq(faq);
        model.addAttribute(FORM_SAVED_FLAG, true);

        return VIEW_NAME;
    }

}
