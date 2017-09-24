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

import static org.jbb.permissions.api.permission.domain.MemberPermissions.CAN_VIEW_FAQ;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jbb.frontend.api.faq.Faq;
import org.jbb.frontend.api.faq.FaqCategory;
import org.jbb.frontend.api.faq.FaqEntry;
import org.jbb.frontend.api.faq.FaqService;
import org.jbb.frontend.web.faq.data.FaqCategoryRow;
import org.jbb.frontend.web.faq.data.FaqEntryRow;
import org.jbb.permissions.api.annotation.MemberPermissionRequired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@MemberPermissionRequired(CAN_VIEW_FAQ)
public class FaqController {
    private static final String FAQ_VIEW_NAME = "faq";

    private final FaqService faqService;

    @RequestMapping("/faq")
    public String getFaq(Model model) {
        Faq faq = faqService.getFaq();
        model.addAttribute("faqCategories", mapToFaqRow(faq));
        return FAQ_VIEW_NAME;
    }

    private List<FaqCategoryRow> mapToFaqRow(Faq faq) {
        return faq.getFaqCategories().stream()
            .map(this::mapToFaqCategoryRow)
            .collect(Collectors.toList());
    }

    private FaqCategoryRow mapToFaqCategoryRow(FaqCategory category) {
        FaqCategoryRow categoryRow = new FaqCategoryRow();
        categoryRow.setName(category.getName());
        categoryRow.setEntries(
            category.getQuestions().stream()
                .map(this::mapToFaqEntryRow)
                .collect(Collectors.toList())
        );
        return categoryRow;
    }

    private FaqEntryRow mapToFaqEntryRow(FaqEntry entry) {
        FaqEntryRow entryRow = new FaqEntryRow();
        entryRow.setQuestion(entry.getQuestion());
        entryRow.setAnswer(entry.getAnswer());
        return entryRow;
    }

}
