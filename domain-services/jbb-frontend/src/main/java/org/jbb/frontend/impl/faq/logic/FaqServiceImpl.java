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

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.jbb.frontend.api.faq.Faq;
import org.jbb.frontend.api.faq.FaqService;
import org.jbb.frontend.impl.faq.dao.FaqCategoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final FaqCategoryRepository faqCategoryRepository;

    @Override
    public Faq getFaq() {
        // todo
        return null;
    }

    @Override
    public void setFaq(Faq faq) {
        Validate.notNull(faq);
        // todo

    }
}
