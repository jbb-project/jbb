/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.install.auto;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.jbb.install.InstallationData;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AdminAutoInstallDataReaderTest {

    @Test
    public void willUpdateAdminData_andBoardName() {
        // given
        InstallationData data = mock(InstallationData.class);

        // when
        InstallationData updateData = new AdminAutoInstallDataReader().updateInstallationData(data, mock(FileBasedConfiguration.class));

        // then
        assertThat(updateData).isSameAs(data);
        verify(data).setAdminUsername(any());
        verify(data).setAdminDisplayedName(any());
        verify(data).setAdminEmail(any());
        verify(data).setAdminPassword(any());
        verify(data).setBoardName(any());
    }
}