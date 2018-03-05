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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidDatabaseInstallationDataValidator implements ConstraintValidator<ValidDatabaseInstallationData, DatabaseInstallationData> {

    @Override
    public void initialize(ValidDatabaseInstallationData constraintAnnotation) {
        // not needed
    }

    @Override
    public boolean isValid(DatabaseInstallationData data, ConstraintValidatorContext context) {
        return true;
    }
}
