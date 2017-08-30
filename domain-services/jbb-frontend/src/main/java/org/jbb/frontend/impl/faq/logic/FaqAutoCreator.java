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
import com.google.common.eventbus.Subscribe;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jbb.frontend.impl.faq.dao.FaqCategoryRepository;
import org.jbb.frontend.impl.faq.model.FaqCategoryEntity;
import org.jbb.frontend.impl.faq.model.FaqEntryEntity;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.system.event.DatabaseSettingsChangedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FaqAutoCreator {

    private final FaqCategoryRepository faqCategoryRepository;
    private final JbbEventBus eventBus;

    @PostConstruct
    public void registerToEventBus() {
        eventBus.register(this);
    }

    @Subscribe
    @Transactional
    public void buildFaq(DatabaseSettingsChangedEvent e) {
        if (faqIsEmpty()) {
            FaqEntryEntity firstFaqEntry = FaqEntryEntity.builder()
                .question("What is jBB?")
                .answer("jBB is a bulletin board software")
                .position(1)
                .build();

            FaqEntryEntity secondFaqEntry = FaqEntryEntity.builder()
                .question("How can I get support?")
                .answer("Visit https://github.com/jbb-project/jbb")
                .position(2)
                .build();

            FaqCategoryEntity firstCategory = FaqCategoryEntity.builder()
                .name("General")
                .entries(Lists.newArrayList(firstFaqEntry, secondFaqEntry))
                .position(1)
                .build();

            firstFaqEntry.setCategory(firstCategory);
            secondFaqEntry.setCategory(firstCategory);

            faqCategoryRepository.save(firstCategory);
        }

    }

    private boolean faqIsEmpty() {
        return faqCategoryRepository.count() == 0;
    }
}
