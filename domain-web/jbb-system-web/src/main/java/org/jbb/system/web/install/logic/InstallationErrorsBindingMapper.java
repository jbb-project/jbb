/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.install.logic;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.Set;

import javax.validation.ConstraintViolation;

@Component
public class InstallationErrorsBindingMapper {
    public static final String DB_INSTALL_DATA = "databaseInstallationData";
    public static final String DATABASE_TYPE = DB_INSTALL_DATA + ".databaseType";

    public static final String H2_EMBEDDED = DB_INSTALL_DATA + ".h2EmbeddedInstallationData";
    public static final String H2_MANAGED = DB_INSTALL_DATA + ".h2ManagedServerInstallationData";
    public static final String H2_REMOTE = DB_INSTALL_DATA + ".h2RemoteServerInstallationData";
    public static final String POSTGRES = DB_INSTALL_DATA + ".postgresqlInstallationData";

    public void map(Set<ConstraintViolation<?>> constraintViolations, BindingResult bindingResult) {
        for (ConstraintViolation violation : constraintViolations) {
            String propertyPath = violation.getPropertyPath().toString();
            propertyPath = propertyPath.replace(DATABASE_TYPE, "databaseProviderName");
            propertyPath = propertyPath.replace(H2_EMBEDDED, "h2embeddedForm");
            propertyPath = propertyPath.replace(H2_MANAGED, "h2managedServerForm");
            propertyPath = propertyPath.replace(H2_REMOTE, "h2remoteServerForm");
            propertyPath = propertyPath.replace(POSTGRES, "postgresqlForm");
            bindingResult.rejectValue(propertyPath, "x", violation.getMessage());
        }
    }
}
