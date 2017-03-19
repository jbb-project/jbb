/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.api.faq;

import java.util.List;

public interface FaqService {
    List<FaqCategory> getFaq();

    FaqCategory addCategoryLast(FaqCategory category);

    void removeCategory(Long categoryId);

    FaqCategory moveCategoryToPosition(Long categoryId, Integer position);

    FaqQuestionAnswer addQuestionLastToCategory(FaqQuestionAnswer question, Long categoryId);

    void removeQuestion(Long questionId);

    FaqQuestionAnswer moveQuestionToPosition(Long questionId, Integer position);

    void moveQuestionToCategory(Long questionId, Long categoryId);


}
