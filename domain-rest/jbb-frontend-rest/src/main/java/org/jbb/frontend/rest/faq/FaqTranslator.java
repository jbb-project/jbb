/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.rest.faq;

import org.jbb.frontend.api.faq.Faq;
import org.jbb.frontend.api.faq.FaqCategory;
import org.jbb.frontend.api.faq.FaqEntry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Component("FaqDtoTranslator")
public class FaqTranslator {

    public Faq toModel(FaqDto dto) {
        return Faq.builder()
                .categories(buildCategories(dto))
                .build();
    }

    private List<FaqCategory> buildCategories(FaqDto dto) {
        return dto.getCategories().stream()
                .map(this::toCategory)
                .collect(Collectors.toList());
    }

    private FaqCategory toCategory(FaqCategoryDto categoryDto) {
        return FaqCategoryImpl.builder()
                .name(categoryDto.getName())
                .questions(buildEntries(categoryDto))
                .build();
    }

    private List<FaqEntry> buildEntries(FaqCategoryDto categoryDto) {
        return categoryDto.getQuestions().stream()
                .map(this::toEntry)
                .collect(Collectors.toList());
    }

    private FaqEntry toEntry(FaqEntryDto entryDto) {
        return FaqEntryImpl.builder()
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

    private FaqCategoryDto toCategoryDto(FaqCategory category) {
        return FaqCategoryDto.builder()
                .name(category.getName())
                .questions(buildEntriesDto(category.getQuestions()))
                .build();
    }

    private List<FaqEntryDto> buildEntriesDto(List<FaqEntry> questions) {
        return questions.stream()
                .map(this::toEntryDto)
                .collect(Collectors.toList());
    }

    private FaqEntryDto toEntryDto(FaqEntry question) {
        return FaqEntryDto.builder()
                .question(question.getQuestion())
                .answer(question.getAnswer())
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class FaqCategoryImpl implements FaqCategory {

        private Long id;
        private String name;
        private List<FaqEntry> questions;

    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class FaqEntryImpl implements FaqEntry {

        private Long id;
        private String question;
        private String answer;

    }
}
