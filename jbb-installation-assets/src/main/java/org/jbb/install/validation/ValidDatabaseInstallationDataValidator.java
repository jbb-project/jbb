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
import org.jbb.install.database.H2EmbeddedInstallationData;
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
    public boolean isValid(DatabaseInstallationData data, ConstraintValidatorContext context) {
        DatabaseType databaseType = data.getDatabaseType();
        if (databaseType == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("You should specify database type")
                    .addPropertyNode("databaseType").addConstraintViolation();
            return false;
        }

        switch (databaseType) {
            case H2_EMBEDDED:
                H2EmbeddedInstallationData dbSpecificData = data.getH2EmbeddedInstallationData();
                Set<ConstraintViolation<H2EmbeddedInstallationData>> violations = validator.validate(dbSpecificData);
                if (!violations.isEmpty()) {
                    context.disableDefaultConstraintViolation();
                    violations.forEach(violation ->
                            context.buildConstraintViolationWithTemplate(violation.getMessageTemplate())
                                    .addPropertyNode("h2EmbeddedInstallationData." + violation.getPropertyPath().toString())
                                    .addConstraintViolation());
                    return false;
                }
        }
        return true;
    }
}
