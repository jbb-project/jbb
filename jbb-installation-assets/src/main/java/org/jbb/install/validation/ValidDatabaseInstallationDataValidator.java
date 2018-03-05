/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.install.validation;


import org.jbb.install.database.DatabaseInstallationData;
import org.jbb.install.database.DatabaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ValidDatabaseInstallationDataValidator implements ConstraintValidator<ValidDatabaseInstallationData, DatabaseInstallationData> {

    @Autowired
    @Qualifier("localValidatorFactoryBean")
    private Validator validator;

    @Override
    public void initialize(ValidDatabaseInstallationData constraintAnnotation) {
        // not needed...
    }

    @Override
    public boolean isValid(DatabaseInstallationData data, ConstraintValidatorContext ctx) {
        DatabaseType databaseType = data.getDatabaseType();
        if (databaseType == null) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate("You should specify database provider")
                    .addPropertyNode("databaseType").addConstraintViolation();
            return false;
        }

        switch (databaseType) {
            case H2_IN_MEMORY:
                return true;
            case H2_EMBEDDED:
                return validateVendorData(data.getH2EmbeddedInstallationData(), "h2EmbeddedInstallationData", ctx);
            case H2_MANAGED_SERVER:
                return validateVendorData(data.getH2ManagedServerInstallationData(), "h2ManagedServerInstallationData", ctx);
            case H2_REMOTE_SERVER:
                return validateVendorData(data.getH2RemoteServerInstallationData(), "h2RemoteServerInstallationData", ctx);
            case POSTGRESQL:
                return validateVendorData(data.getPostgresqlInstallationData(), "postgresqlInstallationData", ctx);
            default:
                return true;
        }
    }

    private boolean validateVendorData(Object data, String parentPropertyNodeName, ConstraintValidatorContext ctx) {
        Set<ConstraintViolation<Object>> violations = validator.validate(data);
        if (!violations.isEmpty()) {
            ctx.disableDefaultConstraintViolation();
            violations.forEach(violation -> buildContraintViolation(violation, parentPropertyNodeName, ctx));
            return false;
        }
        return true;
    }

    private void buildContraintViolation(ConstraintViolation<Object> violation, String parentPropertyNodeName, ConstraintValidatorContext ctx) {
        ctx.buildConstraintViolationWithTemplate(violation.getMessageTemplate())
                .addPropertyNode(parentPropertyNodeName + "." + violation.getPropertyPath().toString())
                .addConstraintViolation();
    }
}
