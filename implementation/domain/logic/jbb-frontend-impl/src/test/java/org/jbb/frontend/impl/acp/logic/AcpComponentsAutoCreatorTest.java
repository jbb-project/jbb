/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.acp.logic;

import org.jbb.frontend.impl.acp.dao.AcpCategoryRepository;
import org.jbb.frontend.impl.acp.dao.AcpElementRepository;
import org.jbb.frontend.impl.acp.dao.AcpSubcategoryRepository;
import org.jbb.frontend.impl.acp.model.AcpCategoryEntity;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.system.event.ConnectionToDatabaseEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AcpComponentsAutoCreatorTest {
    @Mock
    private JbbEventBus eventBusMock;

    @Mock
    private AcpCategoryFactory acpCategoryFactoryMock;

    @Mock
    private AcpSubcategoryFactory acpSubcategoryFactoryMock;

    @Mock
    private AcpCategoryRepository acpCategoryRepositoryMock;

    @Mock
    private AcpSubcategoryRepository acpSubcategoryRepositoryMock;

    @Mock
    private AcpElementRepository acpElementRepositoryMock;

    @InjectMocks
    private AcpComponentsAutoCreator acpComponentsAutoCreator;

    @Test
    public void shouldBuild_whenAcpTablesAreEmpty() throws Exception {
        // given
        given(acpCategoryRepositoryMock.count()).willReturn(0L);
        given(acpSubcategoryRepositoryMock.count()).willReturn(0L);
        given(acpElementRepositoryMock.count()).willReturn(0L);

        // when
        acpComponentsAutoCreator.buildAcp(new ConnectionToDatabaseEvent());

        // then
        verify(acpCategoryRepositoryMock, atLeastOnce()).save(any(AcpCategoryEntity.class));
    }

    @Test
    public void shouldNotBuild_whenAcpTablesAreNotEmpty() throws Exception {
        // given
        given(acpCategoryRepositoryMock.count()).willReturn(1L);
        given(acpSubcategoryRepositoryMock.count()).willReturn(0L);
        given(acpElementRepositoryMock.count()).willReturn(0L);

        // when
        acpComponentsAutoCreator.buildAcp(new ConnectionToDatabaseEvent());

        // then
        verify(acpCategoryRepositoryMock, times(0)).save(any(AcpCategoryEntity.class));
    }

}