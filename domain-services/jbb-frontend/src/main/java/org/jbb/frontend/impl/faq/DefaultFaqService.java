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

import org.apache.commons.lang3.Validate;
import org.jbb.frontend.api.faq.Faq;
import org.jbb.frontend.api.faq.FaqCategory;
import org.jbb.frontend.api.faq.FaqException;
import org.jbb.frontend.api.faq.FaqService;
import org.jbb.frontend.event.FaqChangedEvent;
import org.jbb.frontend.impl.faq.dao.FaqCategoryRepository;
import org.jbb.lib.eventbus.JbbEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultFaqService implements FaqService {

    private final FaqCategoryRepository faqCategoryRepository;
    private final FaqDomainTranslator domainTranslator;
    private final Validator validator;
    private final JbbEventBus eventBus;

    @Override
    public Faq getFaq() {
        List<FaqCategory> faqCategories = faqCategoryRepository.findByOrderByPosition().stream()
                .map(domainTranslator::toModel)
                .collect(Collectors.toList());
        return Faq.builder().categories(faqCategories).build();
    }

    @Override
    @Transactional
    public void setFaq(Faq faq) {
        Validate.notNull(faq);

        ValidatedFaq validatedFaq = domainTranslator.toEntity(faq);

        Set<ConstraintViolation<ValidatedFaq>> constraintViolations = validator.validate(validatedFaq);
        if (!constraintViolations.isEmpty()) {
            throw new FaqException(constraintViolations);
        }

        faqCategoryRepository.deleteAll();
        faqCategoryRepository.saveAll(validatedFaq.getCategories());

        eventBus.post(new FaqChangedEvent());

    }
}
