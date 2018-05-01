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

import org.jbb.system.web.database.form.H2EmbeddedForm;
import org.jbb.system.web.database.form.H2ManagedServerForm;
import org.jbb.system.web.database.form.H2RemoteServerForm;
import org.jbb.system.web.database.form.PostgresqlForm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstallForm {

    private String adminUsername;
    private String adminDisplayedName;
    private String adminEmail;
    private String adminPassword;
    private String adminPasswordAgain;

    private String boardName;

    private String databaseProviderName;

    private H2EmbeddedForm h2embeddedForm = new H2EmbeddedForm();

    private H2ManagedServerForm h2managedServerForm = new H2ManagedServerForm();

    private H2RemoteServerForm h2remoteServerForm = new H2RemoteServerForm();

    private PostgresqlForm postgresqlForm = new PostgresqlForm();

}
