/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.faq;

import org.jbb.frontend.impl.faq.dao.FaqCategoryRepository;
import org.jbb.frontend.impl.faq.model.FaqCategoryEntity;
import org.jbb.install.InstallationData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FaqInstallActionTest {

    @Mock
    private FaqCategoryRepository faqCategoryRepositoryMock;

    @InjectMocks
    private FaqInstallAction faqInstallAction;

    @Test
    public void shouldBuild_whenFaqAreEmpty() throws Exception {
        // when
        faqInstallAction.install(mock(InstallationData.class));

        // then
        verify(faqCategoryRepositoryMock, atLeastOnce()).save(nullable(FaqCategoryEntity.class));
    }

}