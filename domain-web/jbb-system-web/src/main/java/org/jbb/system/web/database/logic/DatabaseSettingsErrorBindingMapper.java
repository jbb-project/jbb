/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.database.logic;

import java.util.Set;
import javax.validation.ConstraintViolation;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class DatabaseSettingsErrorBindingMapper {
    public void map(Set<ConstraintViolation<?>> constraintViolations, BindingResult bindingResult) {
        for (ConstraintViolation violation : constraintViolations) {
            String propertyPath = violation.getPropertyPath().toString();

            if (propertyPath.startsWith("commonSettings.")) {
                propertyPath = propertyPath.replaceFirst("commonSettings.", "");
            } else if (propertyPath.startsWith("h2ManagedServerSettings.")) {
                propertyPath = propertyPath
                    .replaceFirst("h2ManagedServerSettings.", "h2managedServerSettings.");
            }
            bindingResult.rejectValue(propertyPath, "databaseSettings", violation.getMessage());
        }
    }
}
