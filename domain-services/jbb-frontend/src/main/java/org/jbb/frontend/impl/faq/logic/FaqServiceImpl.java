/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.faq.logic;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.jbb.frontend.api.faq.Faq;
import org.jbb.frontend.api.faq.FaqCategory;
import org.jbb.frontend.api.faq.FaqEntry;
import org.jbb.frontend.api.faq.FaqException;
import org.jbb.frontend.api.faq.FaqService;
import org.jbb.frontend.event.FaqChangedEvent;
import org.jbb.frontend.impl.faq.dao.FaqCategoryRepository;
import org.jbb.frontend.impl.faq.model.FaqCategoryEntity;
import org.jbb.frontend.impl.faq.model.FaqEntryEntity;
import org.jbb.lib.eventbus.JbbEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final FaqCategoryRepository faqCategoryRepository;
    private final Validator validator;
    private final JbbEventBus eventBus;

    @Override
    public Faq getFaq() {
        List<FaqCategory> faqCategories = faqCategoryRepository.findByOrderByPosition().stream()
            .map(entity -> (FaqCategory) entity)
            .collect(Collectors.toList());
        return Faq.builder().categories(faqCategories).build();
    }

    @Override
    @Transactional
    public void setFaq(Faq faq) {
        Validate.notNull(faq);

        Set<ConstraintViolation<Faq>> constraintViolations = validator.validate(faq);
        if (!constraintViolations.isEmpty()) {
            throw new FaqException(constraintViolations);
        }

        faqCategoryRepository.deleteAll();

        List<FaqCategory> faqCategories = faq.getCategories();
        int categoryCount = faqCategories.size();

        for (int i = 1; i <= categoryCount; i++) {
            FaqCategoryEntity category = createCategoryEntity(faqCategories.get(i - 1), i);
            faqCategoryRepository.save(category);
        }

        eventBus.post(new FaqChangedEvent());

    }

    private FaqCategoryEntity createCategoryEntity(FaqCategory faqCategory, int position) {
        FaqCategoryEntity category = FaqCategoryEntity.builder()
            .position(position)
            .name(faqCategory.getName())
            .build();

        category.setEntries(createEntryEntities(faqCategory.getQuestions(), category));

        return category;
    }

    private List<FaqEntryEntity> createEntryEntities(List<FaqEntry> questions,
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
