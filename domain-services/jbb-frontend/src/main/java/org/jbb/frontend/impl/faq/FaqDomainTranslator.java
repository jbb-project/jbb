/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.faq;

import com.google.common.collect.Lists;

import org.jbb.frontend.api.faq.Faq;
import org.jbb.frontend.impl.faq.model.FaqCategoryEntity;
import org.jbb.frontend.impl.faq.model.FaqEntryEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
class FaqDomainTranslator {

    ValidatedFaq toEntity(Faq faq) {
        List<Faq.Category> faqCategories = faq.getCategories();
        int categoryCount = faqCategories.size();

        List<FaqCategoryEntity> result = Lists.newArrayList();
        for (int i = 1; i <= categoryCount; i++) {
            FaqCategoryEntity category = toCategoryEntity(faqCategories.get(i - 1), i);
            result.add(category);
        }
        return ValidatedFaq.builder().categories(result).build();
    }

    Faq.Category toModel(FaqCategoryEntity entity) {
        return Faq.Category.builder()
                .name(entity.getName())
                .questions(entity.getEntries().stream()
                        .map(this::toModel)
                        .collect(Collectors.toList())
                )
                .build();
    }

    private Faq.Entry toModel(FaqEntryEntity entity) {
        return Faq.Entry.builder()
                .question(entity.getQuestion())
                .answer(entity.getAnswer())
                .build();
    }

    FaqCategoryEntity toCategoryEntity(Faq.Category faqCategory, int position) {
        FaqCategoryEntity category = FaqCategoryEntity.builder()
                .position(position)
                .name(faqCategory.getName())
                .build();

        category.setEntries(createEntryEntities(faqCategory.getQuestions(), category));

        return category;
    }

    private List<FaqEntryEntity> createEntryEntities(List<Faq.Entry> questions,
                                                     FaqCategoryEntity category) {

        int questionCount = questions.size();
        List<FaqEntryEntity> entries = Lists.newArrayList();
        for (int i = 1; i <= questionCount; i++) {
            entries.add(FaqEntryEntity.builder()
                    .position(i)
                    .category(category)
                    .question(questions.get(i - 1).getQuestion())
                    .answer(questions.get(i - 1).getAnswer()).build()
            );
        }
        return entries;
    }
}
