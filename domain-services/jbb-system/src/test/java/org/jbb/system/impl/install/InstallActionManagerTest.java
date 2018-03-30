/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.install;

import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.install.JbbVersions;
import org.jbb.system.api.install.InstallationException;
import org.jbb.system.impl.install.dao.InstalledStepRepository;
import org.jbb.system.impl.install.model.InstalledStepEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InstallActionManagerTest {
    @Mock
    private InstalledStepRepository installedStepRepositoryMock;

    @InjectMocks
    private InstallActionManager installActionManager;

    @Test(expected = InstallationException.class)
    public void whenInstallationFailed_whenThrowInstallationException() {
        // given
        InstallUpdateAction action = mock(InstallUpdateAction.class);
        willThrow(new RuntimeException()).given(action).install(any());

        // when
        installActionManager.install(action, null);

        // then
        // throw InstallationException
    }

    @Test
    public void willSaveSuccessfulStep_whenInstallationSucceed() {
        // given
        InstallUpdateAction action = mock(InstallUpdateAction.class);
        given(action.fromVersion()).willReturn(JbbVersions.VERSION_0_3_0);

        // when
        installActionManager.install(action, mock(InstallationData.class));

        // then
        verify(installedStepRepositoryMock).save(any(InstalledStepEntity.class));

    }
}