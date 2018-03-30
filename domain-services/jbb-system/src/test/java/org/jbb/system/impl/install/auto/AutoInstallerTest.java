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

import org.jbb.install.InstallationData;
import org.jbb.system.api.install.InstallationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AutoInstallerTest {

    @Mock
    private AutoInstallationFileManager autoInstallationFileManagerMock;

    @Mock
    private InstallationService installationServiceMock;

    @InjectMocks
    private AutoInstaller autoInstaller;

    @Test
    public void alwaysTryToRemoveInstallFile() {
        // given
        given(installationServiceMock.isInstalled()).willReturn(true);

        // when
        autoInstaller.autoInstallIfApplicable();

        // then
        verify(autoInstallationFileManagerMock).removeAutoInstallFile();
    }

    @Test
    public void runInstallationWhen_notInstalled_andAutoinstallFileExists() {
        // given
        given(installationServiceMock.isInstalled()).willReturn(false);
        given(autoInstallationFileManagerMock.readAutoInstallFile())
                .willReturn(Optional.of(mock(InstallationData.class)));

        // when
        autoInstaller.autoInstallIfApplicable();

        // then
        verify(installationServiceMock).install(any());
    }

    @Test
    public void doNotRunInstallationWhen_notInstalled_andAutoinstallFileDoesNotExist() {
        // given
        given(installationServiceMock.isInstalled()).willReturn(false);
        given(autoInstallationFileManagerMock.readAutoInstallFile())
                .willReturn(Optional.empty());

        // when
        autoInstaller.autoInstallIfApplicable();

        // then
        verify(installationServiceMock, times(0)).install(any());
    }
}