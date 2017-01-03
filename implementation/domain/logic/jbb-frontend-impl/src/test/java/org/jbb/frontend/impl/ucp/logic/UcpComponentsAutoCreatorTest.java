/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp.logic;

import org.jbb.frontend.impl.ucp.dao.UcpCategoryRepository;
import org.jbb.frontend.impl.ucp.dao.UcpElementRepository;
import org.jbb.frontend.impl.ucp.model.UcpCategoryEntity;
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
public class UcpComponentsAutoCreatorTest {
    @Mock
    private JbbEventBus eventBusMock;

    @Mock
    private UcpCategoryFactory ucpCategoryFactoryMock;

    @Mock
    private UcpCategoryRepository categoryRepositoryMock;

    @Mock
    private UcpElementRepository elementRepositoryMock;

    @InjectMocks
    private UcpComponentsAutoCreator ucpComponentsAutoCreator;


    @Test
    public void shouldBuild_whenUcpTablesAreEmpty() throws Exception {
        // given
        given(categoryRepositoryMock.count()).willReturn(0L);
        given(elementRepositoryMock.count()).willReturn(0L);

        // when
        ucpComponentsAutoCreator.buildUcp(new ConnectionToDatabaseEvent());

        // then
        verify(categoryRepositoryMock, atLeastOnce()).save(any(UcpCategoryEntity.class));
    }

    @Test
    public void shouldNotBuild_whenUcpTablesAreNotEmpty() throws Exception {
        // given
        given(categoryRepositoryMock.count()).willReturn(1L);
        given(elementRepositoryMock.count()).willReturn(1L);

        // when
        ucpComponentsAutoCreator.buildUcp(new ConnectionToDatabaseEvent());

        // then
        verify(categoryRepositoryMock, times(0)).save(any(UcpCategoryEntity.class));
    }
}