/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.privilege.health;


import com.codahale.metrics.health.HealthCheck;

import org.jbb.security.impl.privilege.dao.AdministratorRepository;
import org.jbb.system.api.install.InstallationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AdministratorExistsHealthCheckTest {

    @Mock
    private InstallationService installationServiceMock;

    @Mock
    private AdministratorRepository administratorRepositoryMock;

    @InjectMocks
    private AdministratorExistsHealthCheck administratorExistsHealthCheck;

    @Test
    public void shouldReturnNotEmptyName() {
        // when
        String checkName = administratorExistsHealthCheck.getName();

        // then
        assertThat(checkName).isNotBlank();
    }

    @Test
    public void shouldBeHealthy_whenInstalledAndAdministratorExists() throws Exception {
        // given
        given(installationServiceMock.isInstalled()).willReturn(true);
        given(administratorRepositoryMock.count()).willReturn(1L);

        // when
        HealthCheck.Result result = administratorExistsHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isTrue();
    }

    @Test
    public void shouldBeHealthy_whenNotInstalledAndAdministratorExists() throws Exception {
        // given
        given(installationServiceMock.isInstalled()).willReturn(false);
        given(administratorRepositoryMock.count()).willReturn(1L);

        // when
        HealthCheck.Result result = administratorExistsHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isTrue();
    }

    @Test
    public void shouldBeHealthy_whenNotInstalledAndAdministratorNotExist() throws Exception {
        // given
        given(installationServiceMock.isInstalled()).willReturn(false);
        given(administratorRepositoryMock.count()).willReturn(0L);

        // when
        HealthCheck.Result result = administratorExistsHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isTrue();
    }

    @Test
    public void shouldBeUnhealthy_whenInstalledAndAdministratorNotExist() throws Exception {
        // given
        given(installationServiceMock.isInstalled()).willReturn(true);
        given(administratorRepositoryMock.count()).willReturn(0L);

        // when
        HealthCheck.Result result = administratorExistsHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isFalse();
    }


}