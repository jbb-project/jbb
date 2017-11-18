/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.jbb.frontend.impl.ucp.dao.UcpCategoryRepository;
import org.jbb.frontend.impl.ucp.dao.UcpElementRepository;
import org.jbb.frontend.impl.ucp.model.UcpCategoryEntity;
import org.jbb.install.InstallationData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UcpInstallActionTest {

    @Mock
    private UcpCategoryFactory ucpCategoryFactoryMock;

    @Mock
    private UcpCategoryRepository categoryRepositoryMock;

    @Mock
    private UcpElementRepository elementRepositoryMock;

    @InjectMocks
    private UcpInstallAction ucpInstallAction;


    @Test
    public void shouldBuild_whenUcpTablesAreEmpty() throws Exception {
        // given
        given(categoryRepositoryMock.count()).willReturn(0L);
        given(elementRepositoryMock.count()).willReturn(0L);

        // when
        ucpInstallAction.install(mock(InstallationData.class));

        // then
        verify(categoryRepositoryMock, atLeastOnce()).save(nullable(UcpCategoryEntity.class));
    }

    @Test
    public void shouldNotBuild_whenUcpTablesAreNotEmpty() throws Exception {
        // given
        given(categoryRepositoryMock.count()).willReturn(1L);

        // when
        ucpInstallAction.install(mock(InstallationData.class));

        // then
        verify(categoryRepositoryMock, times(0)).save(any(UcpCategoryEntity.class));
    }

}