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

import org.jbb.install.InstallationData;
import org.jbb.install.database.DatabaseInstallationData;
import org.jbb.install.database.DatabaseType;
import org.jbb.system.api.install.InstallationService;
import org.jbb.system.impl.BaseIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class InstallationServiceIT extends BaseIT {

    @Autowired
    private InstallationService installationService;

    @Test
    public void shouldInstallSuccessful() {
        // given
        InstallationData data = validInstallationData();
        assertThat(installationService.isInstalled()).isFalse();
        assertThat(installationService.getInstalledSteps()).isEmpty();

        // when
        installationService.install(data);

        // then
        assertThat(installationService.isInstalled()).isTrue();
        assertThat(installationService.getInstalledSteps()).isNotEmpty();
    }

    private InstallationData validInstallationData() {
        return InstallationData.builder()
                .adminUsername("admin")
                .adminDisplayedName("Admin")
                .adminEmail("admin@admin.com")
                .adminPassword("admin")
                .boardName("jBB Testing")
                .databaseInstallationData(
                        DatabaseInstallationData.builder()
                                .databaseType(DatabaseType.H2_IN_MEMORY)
                                .build()
                )
                .build();
    }
}
