/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp.install;

import org.jbb.frontend.impl.ucp.UcpCategoryFactory;
import org.jbb.frontend.impl.ucp.dao.UcpCategoryRepository;
import org.jbb.frontend.impl.ucp.model.UcpCategoryEntity;
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
public class UcpInstallActionTest {

    @Mock
    private UcpCategoryFactory ucpCategoryFactoryMock;

    @Mock
    private UcpCategoryRepository categoryRepositoryMock;

    @InjectMocks
    private UcpInstallAction ucpInstallAction;


    @Test
    public void shouldBuild_whenUcpTablesAreEmpty() throws Exception {
        // when
        ucpInstallAction.install(mock(InstallationData.class));

        // then
        verify(categoryRepositoryMock, atLeastOnce()).save(nullable(UcpCategoryEntity.class));
    }

}