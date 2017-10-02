/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.acp.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AcpCategoryDescriptionControllerTest {

    @InjectMocks
    AcpCategoryDescriptionController acpCategoryDescriptionController;

    @Test
    public void shouldReturnGeneralAcpView() throws Exception {
        assertThat(acpCategoryDescriptionController.general()).isEqualTo("acp/general");
    }

    @Test
    public void shouldReturnMembersAcpView() throws Exception {
        assertThat(acpCategoryDescriptionController.members()).isEqualTo("acp/members");
    }

    @Test
    public void shouldReturnSystemAcpView() throws Exception {
        assertThat(acpCategoryDescriptionController.system()).isEqualTo("acp/system");
    }
}
