/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.rest.faq;

import org.jbb.frontend.api.faq.Faq;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("FaqDtoTranslator")
public class FaqTranslator {

    public Faq toModel(FaqDto dto) {
        return Faq.builder()
                .categories(buildCategories(dto))
                .build();
    }

    private List<Faq.Category> buildCategories(FaqDto dto) {
        return dto.getCategories().stream()
                .map(this::toCategory)
                .collect(Collectors.toList());
    }

    private Faq.Category toCategory(FaqCategoryDto categoryDto) {
        return Faq.Category.builder()
                .name(categoryDto.getName())
                .questions(buildEntries(categoryDto))
                .build();
    }

    private List<Faq.Entry> buildEntries(FaqCategoryDto categoryDto) {
        return categoryDto.getQuestions().stream()
                .map(this::toEntry)
                .collect(Collectors.toList());
    }

    private Faq.Entry toEntry(FaqEntryDto entryDto) {
        return Faq.Entry.builder()
                .question(entryDto.getQuestion())
                .answer(entryDto.getAnswer())
                .build();
    }

    public FaqDto toDto(Faq faq) {
        return FaqDto.builder()
                .categories(buildCategoriesDto(faq))
                .build();
    }

    private List<FaqCategoryDto> buildCategoriesDto(Faq faq) {
        return faq.getCategories().stream()
                .map(this::toCategoryDto)
                .collect(Collectors.toList());
    }

    private FaqCategoryDto toCategoryDto(Faq.Category category) {
        return FaqCategoryDto.builder()
                .name(category.getName())
                .questions(buildEntriesDto(category.getQuestions()))
                .build();
    }

    private List<FaqEntryDto> buildEntriesDto(List<Faq.Entry> questions) {
        return questions.stream()
                .map(this::toEntryDto)
                .collect(Collectors.toList());
    }

    private FaqEntryDto toEntryDto(Faq.Entry question) {
        return FaqEntryDto.builder()
                .question(question.getQuestion())
                .answer(question.getAnswer())
                .build();
    }

}
