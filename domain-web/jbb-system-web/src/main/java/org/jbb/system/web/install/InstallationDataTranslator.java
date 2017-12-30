/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.install;

import org.apache.commons.lang3.EnumUtils;
import org.jbb.install.InstallationData;
import org.jbb.install.database.DatabaseInstallationData;
import org.jbb.install.database.DatabaseType;
import org.jbb.install.database.H2EmbeddedInstallationData;
import org.jbb.install.database.H2ManagedServerInstallationData;
import org.jbb.install.database.H2RemoteServerInstallationData;
import org.jbb.install.database.PostgresqlInstallationData;
import org.jbb.system.web.database.form.H2EmbeddedForm;
import org.jbb.system.web.database.form.H2ManagedServerForm;
import org.jbb.system.web.database.form.H2RemoteServerForm;
import org.jbb.system.web.database.form.PostgresqlForm;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InstallationDataTranslator {

    public InstallationData transform(InstallForm form) {
        return InstallationData.builder()
                .adminUsername(form.getAdminUsername())
                .adminDisplayedName(form.getAdminDisplayedName())
                .adminEmail(form.getAdminEmail())
                .adminPassword(form.getAdminPassword())
                .boardName(form.getBoardName())
                .databaseInstallationData(buildDatabaseInstallationData(form))
                .cacheInstallationData(Optional.empty())
                .build();
    }

    private DatabaseInstallationData buildDatabaseInstallationData(InstallForm form) {
        H2EmbeddedForm h2embeddedForm = form.getH2embeddedForm();
        H2ManagedServerForm h2managedServerForm = form.getH2managedServerForm();
        H2RemoteServerForm h2remoteServerForm = form.getH2remoteServerForm();
        PostgresqlForm postgresqlForm = form.getPostgresqlForm();

        return DatabaseInstallationData.builder()
                .databaseType(EnumUtils.getEnum(DatabaseType.class, form.getDatabaseProviderName()))

                .h2EmbeddedInstallationData(
                        H2EmbeddedInstallationData.builder()
                                .databaseFileName(h2embeddedForm.getDatabaseFileName())
                                .username(h2embeddedForm.getUsername())
                                .usernamePassword(h2embeddedForm.getUsernamePassword())
                                .build()
                )

                .h2ManagedServerInstallationData(
                        H2ManagedServerInstallationData.builder()
                                .databaseFileName(h2managedServerForm.getDatabaseFileName())
                                .port(h2managedServerForm.getPort())
                                .username(h2managedServerForm.getUsername())
                                .usernamePassword(h2managedServerForm.getUsernamePassword())
                                .build()
                )

                .h2RemoteServerInstallationData(
                        H2RemoteServerInstallationData.builder()
                                .url(h2remoteServerForm.getUrl())
                                .username(h2remoteServerForm.getUsername())
                                .usernamePassword(h2remoteServerForm.getUsernamePassword())
                                .build()
                )

                .postgresqlInstallationData(
                        PostgresqlInstallationData.builder()
                                .hostName(postgresqlForm.getHostName())
                                .port(postgresqlForm.getPort())
                                .databaseName(postgresqlForm.getDatabaseName())
                                .username(postgresqlForm.getUsername())
                                .password(postgresqlForm.getPassword())
                                .build()
                )

                .build();
    }

}
