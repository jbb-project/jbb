/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.base;

import java.text.MessageFormat;
import javax.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.restful.error.ErrorDetail;
import org.jbb.security.api.password.PasswordRequirements;
import org.jbb.security.api.password.PasswordService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberExceptionMapper {

    private final PasswordService passwordService;

    public ErrorDetail mapToErrorDetail(ConstraintViolation<?> violation) {
        String propertyPath = violation.getPropertyPath().toString();
        if ("visiblePassword".equals(propertyPath)) {
            PasswordRequirements requirements = passwordService.currentRequirements();
            return new ErrorDetail("password", MessageFormat
                .format(violation.getMessage(),
                    requirements.getMinimumLength(),
                    requirements.getMaximumLength()));
        }

        return new ErrorDetail(StringUtils.removeEndIgnoreCase(propertyPath, ".value"),
            violation.getMessage());
    }
}
