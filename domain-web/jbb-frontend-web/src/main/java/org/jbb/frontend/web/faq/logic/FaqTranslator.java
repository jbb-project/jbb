/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.faq.logic;

import com.google.common.collect.Lists;

import org.jbb.frontend.api.faq.Faq;
import org.jbb.frontend.web.faq.form.FaqCategoryForm;
import org.jbb.frontend.web.faq.form.FaqEntryForm;
import org.jbb.frontend.web.faq.form.FaqForm;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FaqTranslator {

    public Faq toFaq(FaqForm form) {
        List<Faq.Category> faqCategories = form.getCategories().stream()
                .map(this::mapToCategory)
                .collect(Collectors.toList());
        return Faq.builder()
                .categories(faqCategories)
                .build();
    }

    private Faq.Category mapToCategory(FaqCategoryForm categoryForm) {
        if (categoryForm.getEntries() == null) {
            categoryForm.setEntries(Lists.newArrayList());
        }
        List<Faq.Entry> faqEntries = categoryForm.getEntries().stream()
                .map(this::mapToEntry)
                .collect(Collectors.toList());
        return Faq.Category.builder()
                .name(categoryForm.getName())
                .questions(faqEntries).build();
    }

    private Faq.Entry mapToEntry(FaqEntryForm entry) {
        return Faq.Entry.builder()
                .question(entry.getQuestion())
                .answer(entry.getAnswer())
                .build();
    }

    public FaqForm toForm(Faq faq) {
        List<FaqCategoryForm> categoryForms = faq.getCategories().stream()
                .map(this::mapToCategoryForm)
                .collect(Collectors.toList());

        return new FaqForm(categoryForms);
    }

    private FaqCategoryForm mapToCategoryForm(Faq.Category category) {
        return FaqCategoryForm.builder()
                .name(category.getName())
                .entries(category.getQuestions().stream()
                        .map(this::mapToEntryForm).collect(Collectors.toList()))
                .build();
    }

    private FaqEntryForm mapToEntryForm(Faq.Entry entry) {
        return FaqEntryForm.builder()
                .question(entry.getQuestion())
                .answer(entry.getAnswer())
                .build();
    }

}
