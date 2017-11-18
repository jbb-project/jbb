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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.jbb.frontend.impl.faq.dao.FaqCategoryRepository;
import org.jbb.frontend.impl.faq.model.FaqCategoryEntity;
import org.jbb.install.InstallationData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FaqInstallActionTest {

    @Mock
    private FaqCategoryRepository faqCategoryRepositoryMock;

    @InjectMocks
    private FaqInstallAction faqInstallAction;

    @Test
    public void shouldBuild_whenFaqAreEmpty() throws Exception {
        // given
        given(faqCategoryRepositoryMock.count()).willReturn(0L);

        // when
        faqInstallAction.install(mock(InstallationData.class));

        // then
        verify(faqCategoryRepositoryMock, atLeastOnce()).save(nullable(FaqCategoryEntity.class));
    }

    @Test
    public void shouldNotBuild_whenFaqCategoryTableIsNotEmpty() throws Exception {
        // given
        given(faqCategoryRepositoryMock.count()).willReturn(1L);

        // when
        faqInstallAction.install(mock(InstallationData.class));

        // then
        verify(faqCategoryRepositoryMock, times(0)).save(any(FaqCategoryEntity.class));
    }
}