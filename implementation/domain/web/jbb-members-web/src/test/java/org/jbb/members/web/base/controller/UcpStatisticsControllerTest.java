/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.base.controller;

import org.jbb.lib.core.security.SecurityContentUser;
import org.jbb.members.api.data.RegistrationMetaData;
import org.jbb.members.api.service.RegistrationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UcpStatisticsControllerTest {
    @Mock
    private RegistrationService registrationServiceMock;

    @InjectMocks
    private UcpStatisticsController ucpStatisticsController;

    @Test
    public void shouldSetOverviewStatisticsViewName_andPutJoinTimeToModel() throws Exception {
        // given
        SecurityContentUser userMock = mock(SecurityContentUser.class);
        Authentication authenticationMock = mock(Authentication.class);
        given(authenticationMock.getPrincipal()).willReturn(userMock);
        given(registrationServiceMock.getRegistrationMetaData(any())).willReturn(mock(RegistrationMetaData.class));

        Model modelMock = mock(Model.class);

        // when
        String viewName = ucpStatisticsController.statistics(modelMock, authenticationMock);

        // then
        assertThat(viewName).isEqualTo("ucp/overview/statistics");
        verify(modelMock, times(1)).addAttribute(eq("joinTime"), any());
    }
}