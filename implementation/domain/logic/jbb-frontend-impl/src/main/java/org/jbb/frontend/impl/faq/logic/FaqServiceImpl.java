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

import org.jbb.frontend.api.faq.FaqCategory;
import org.jbb.frontend.api.faq.FaqQuestionAnswer;
import org.jbb.frontend.api.faq.FaqService;
import org.jbb.frontend.impl.faq.dao.FaqCategoryRepository;
import org.jbb.frontend.impl.faq.dao.FaqQuestionAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaqServiceImpl implements FaqService {

    private final FaqCategoryRepository categoryRepository;
    private final FaqQuestionAnswerRepository questionRepository;

    @Autowired
    public FaqServiceImpl(FaqCategoryRepository categoryRepository, FaqQuestionAnswerRepository questionRepository) {
        this.categoryRepository = categoryRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public List<FaqCategory> getFaq() {
        throw new UnsupportedOperationException();
    }

    @Override
    public FaqCategory addCategoryLast(FaqCategory category) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeCategory(Long categoryId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FaqCategory moveCategoryToPosition(Long categoryId, Integer position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FaqQuestionAnswer addQuestionLastToCategory(FaqQuestionAnswer question, Long categoryId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeQuestion(Long questionId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FaqQuestionAnswer moveQuestionToPosition(Long questionId, Integer position) {
        throw new UnsupportedOperationException();
    }
}
