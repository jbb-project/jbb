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

import org.assertj.core.util.Lists;
import org.jbb.install.InstallationData;
import org.jbb.install.cache.CacheInstallationData;
import org.jbb.install.cache.CacheType;
import org.jbb.install.cache.HazelcastClientInstallationData;
import org.jbb.install.cache.HazelcastServerInstallationData;
import org.jbb.install.database.DatabaseInstallationData;
import org.jbb.install.database.DatabaseType;
import org.jbb.install.database.H2EmbeddedInstallationData;
import org.jbb.install.database.H2ManagedServerInstallationData;
import org.jbb.install.database.H2RemoteServerInstallationData;
import org.jbb.install.database.PostgresqlInstallationData;
import org.jbb.system.api.install.InstallationDataException;
import org.jbb.system.api.install.InstallationService;
import org.jbb.system.impl.BaseIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class InstallationServiceValidationIT extends BaseIT {

    @Autowired
    private InstallationService installationService;

    @Test
    public void shouldThrowInstallationException_whenAdminUsernameEmpty() {
        // given
        InstallationData data = validInstallationData();
        data.setAdminUsername(null);

        // when
        try {
            installationService.install(data);
        } catch (InstallationDataException e) {
            shouldHaveViolation(e, "adminUsername", "must not be empty");
            return;
        }

        fail("Should throw InstallationDataException");
    }

    @Test
    public void shouldThrowInstallationException_whenAdminUsernameTooShort() {
        // given
        InstallationData data = validInstallationData();
        data.setAdminUsername("aa");

        // when
        try {
            installationService.install(data);
        } catch (InstallationDataException e) {
            shouldHaveViolation(e, "adminUsername", "size must be between 3 and 20");
            return;
        }

        fail("Should throw InstallationDataException");
    }

    @Test
    public void shouldThrowInstallationException_whenAdminUsernameTooLong() {
        // given
        InstallationData data = validInstallationData();
        data.setAdminUsername("abcdefgh00abcdefgh00abcdefgh00");

        // when
        try {
            installationService.install(data);
        } catch (InstallationDataException e) {
            shouldHaveViolation(e, "adminUsername", "size must be between 3 and 20");
            return;
        }

        fail("Should throw InstallationDataException");
    }

    @Test
    public void shouldThrowInstallationException_whenAdminDisplayedNameEmpty() {
        // given
        InstallationData data = validInstallationData();
        data.setAdminDisplayedName(null);

        // when
        try {
            installationService.install(data);
        } catch (InstallationDataException e) {
            shouldHaveViolation(e, "adminDisplayedName", "must not be empty");
            return;
        }

        fail("Should throw InstallationDataException");
    }

    @Test
    public void shouldThrowInstallationException_whenAdminDisplayedNameTooShort() {
        // given
        InstallationData data = validInstallationData();
        data.setAdminDisplayedName("aa");

        // when
        try {
            installationService.install(data);
        } catch (InstallationDataException e) {
            shouldHaveViolation(e, "adminDisplayedName", "size must be between 3 and 64");
            return;
        }

        fail("Should throw InstallationDataException");
    }

    @Test
    public void shouldThrowInstallationException_whenAdminDisplayedNameTooLong() {
        // given
        InstallationData data = validInstallationData();
        data.setAdminDisplayedName("abcdefgh00abcdefgh00abcdefgh00abcdefgh00abcdefgh00abcdefgh00234543");

        // when
        try {
            installationService.install(data);
        } catch (InstallationDataException e) {
            shouldHaveViolation(e, "adminDisplayedName", "size must be between 3 and 64");
            return;
        }

        fail("Should throw InstallationDataException");
    }

    @Test
    public void shouldThrowInstallationException_whenAdminEmailEmpty() {
        // given
        InstallationData data = validInstallationData();
        data.setAdminEmail(null);

        // when
        try {
            installationService.install(data);
        } catch (InstallationDataException e) {
            shouldHaveViolation(e, "adminEmail", "must not be empty");
            return;
        }

        fail("Should throw InstallationDataException");
    }

    @Test
    public void shouldThrowInstallationException_whenAdminEmailInvalid() {
        // given
        InstallationData data = validInstallationData();
        data.setAdminEmail("aa(AT)onet.pl");

        // when
        try {
            installationService.install(data);
        } catch (InstallationDataException e) {
            shouldHaveViolation(e, "adminEmail", "must be a well-formed email address");
            return;
        }

        fail("Should throw InstallationDataException");
    }

    @Test
    public void shouldThrowInstallationException_whenAdminPasswordEmpty() {
        // given
        InstallationData data = validInstallationData();
        data.setAdminPassword(null);

        // when
        try {
            installationService.install(data);
        } catch (InstallationDataException e) {
            shouldHaveViolation(e, "adminPassword", "must not be blank");
            return;
        }

        fail("Should throw InstallationDataException");
    }

    @Test
    public void shouldThrowInstallationException_whenBoardNameEmpty() {
        // given
        InstallationData data = validInstallationData();
        data.setBoardName(null);

        // when
        try {
            installationService.install(data);
        } catch (InstallationDataException e) {
            shouldHaveViolation(e, "boardName", "must not be blank");
            return;
        }

        fail("Should throw InstallationDataException");
    }

    @Test
    public void shouldThrowInstallationException_whenBoardNameTooLong() {
        // given
        InstallationData data = validInstallationData();
        data.setBoardName("abcdefghi abcdefghi abcdefghi abcdefghi abcdefghi abcdefghi 7777777");

        // when
        try {
            installationService.install(data);
        } catch (InstallationDataException e) {
            shouldHaveViolation(e, "boardName", "length must be between 1 and 60");
            return;
        }

        fail("Should throw InstallationDataException");
    }

    @Test
    public void shouldThrowInstallationException_whenDbInstallationDataNull() {
        // given
        InstallationData data = validInstallationData();
        data.setDatabaseInstallationData(null);

        // when
        try {
            installationService.install(data);
        } catch (InstallationDataException e) {
            shouldHaveViolation(e, "databaseInstallationData", "must not be null");
            return;
        }

        fail("Should throw InstallationDataException");
    }

    @Test
    public void shouldThrowInstallationException_whenNoDatabaseType() {
        // given
        InstallationData data = validInstallationData();
        data.getDatabaseInstallationData().setDatabaseType(null);

        // when
        try {
            installationService.install(data);
        } catch (InstallationDataException e) {
            shouldHaveViolation(e, "databaseInstallationData.databaseType", "You should specify database provider");
            return;
        }

        fail("Should throw InstallationDataException");
    }

    @Test
    public void shouldThrowInstallationException_whenH2Embedded_andBlankUsername() {
        // given
        InstallationData data = validInstallationData();
        data.getDatabaseInstallationData().setDatabaseType(DatabaseType.H2_EMBEDDED);
        data.getDatabaseInstallationData().getH2EmbeddedInstallationData().setUsername("");

        // when
        try {
            installationService.install(data);
        } catch (InstallationDataException e) {
            shouldHaveViolation(e, "databaseInstallationData.h2EmbeddedInstallationData.username",
                    "must not be blank");
            return;
        }

        fail("Should throw InstallationDataException");
    }

    @Test
    public void shouldThrowInstallationException_whenH2Managed_andBlankPassword() {
        // given
        InstallationData data = validInstallationData();
        data.getDatabaseInstallationData().setDatabaseType(DatabaseType.H2_MANAGED_SERVER);
        data.getDatabaseInstallationData().getH2ManagedServerInstallationData().setUsernamePassword("");

        // when
        try {
            installationService.install(data);
        } catch (InstallationDataException e) {
            shouldHaveViolation(e, "databaseInstallationData.h2ManagedServerInstallationData.usernamePassword",
                    "must not be blank");
            return;
        }

        fail("Should throw InstallationDataException");
    }

    @Test
    public void shouldThrowInstallationException_whenH2Remote_andBlankUrl() {
        // given
        InstallationData data = validInstallationData();
        data.getDatabaseInstallationData().setDatabaseType(DatabaseType.H2_REMOTE_SERVER);
        data.getDatabaseInstallationData().getH2RemoteServerInstallationData().setUrl("  ");

        // when
        try {
            installationService.install(data);
        } catch (InstallationDataException e) {
            shouldHaveViolation(e, "databaseInstallationData.h2RemoteServerInstallationData.url",
                    "must not be blank");
            return;
        }

        fail("Should throw InstallationDataException");
    }

    @Test
    public void shouldThrowInstallationException_whenPostgres_andEmptyHostName() {
        // given
        InstallationData data = validInstallationData();
        data.getDatabaseInstallationData().setDatabaseType(DatabaseType.POSTGRESQL);
        data.getDatabaseInstallationData().getPostgresqlInstallationData().setHostName("");

        // when
        try {
            installationService.install(data);
        } catch (InstallationDataException e) {
            shouldHaveViolation(e, "databaseInstallationData.postgresqlInstallationData.hostName",
                    "must not be blank");
            return;
        }

        fail("Should throw InstallationDataException");
    }

    private void shouldHaveViolation(InstallationDataException e, String path, String message) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();

        assertThat(violations).hasSize(1);
        ConstraintViolation<?> violation = violations.stream().findFirst().get();
        assertThat(violation.getPropertyPath().toString()).isEqualTo(path);
        assertThat(violation.getMessage()).isEqualTo(message);
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
                                .h2EmbeddedInstallationData(
                                        H2EmbeddedInstallationData.builder()
                                                .databaseFileName("test")
                                                .username("ppp")
                                                .usernamePassword("ppp")
                                                .build()
                                )
                                .h2ManagedServerInstallationData(
                                        H2ManagedServerInstallationData.builder()
                                                .databaseFileName("test")
                                                .port(8090)
                                                .username("ppp")
                                                .usernamePassword("ppp")
                                                .build()
                                )
                                .h2RemoteServerInstallationData(
                                        H2RemoteServerInstallationData.builder()
                                                .url("localhost:9000")
                                                .username("ppp")
                                                .usernamePassword("ppp")
                                                .build()
                                )
                                .postgresqlInstallationData(
                                        PostgresqlInstallationData.builder()
                                                .hostName("localhost")
                                                .port(5432)
                                                .username("ppp")
                                                .password("ppp")
                                                .databaseName("jbb")
                                                .build()
                                )
                                .build()
                )
                .cacheInstallationData(Optional.of(
                        CacheInstallationData.builder()
                                .cacheType(CacheType.HAZELCAST_SERVER)
                                .hazelcastClientInstallationData(
                                        HazelcastClientInstallationData.builder()
                                                .groupName("ppp")
                                                .groupPassword("ppp")
                                                .members(Lists.newArrayList("a", "b"))
                                                .build()
                                )
                                .hazelcastServerInstallationData(
                                        HazelcastServerInstallationData.builder()
                                                .groupName("ppp")
                                                .groupPassword("ppp")
                                                .serverPort(6868)
                                                .members(Lists.newArrayList("localhost:2222"))
                                                .build()
                                )
                                .build()
                        )
                )
                .build();
    }
}