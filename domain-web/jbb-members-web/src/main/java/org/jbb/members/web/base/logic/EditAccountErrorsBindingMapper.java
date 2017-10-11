/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.base.logic;

import java.text.MessageFormat;
import java.util.Set;
import javax.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jbb.security.api.password.PasswordRequirements;
import org.jbb.security.api.password.PasswordService;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Slf4j
@Component
@RequiredArgsConstructor
public class EditAccountErrorsBindingMapper {

    private final PasswordService passwordService;

    public void map(Set<ConstraintViolation<?>> constraintViolations, BindingResult bindingResult) {

        for (ConstraintViolation violation : constraintViolations) {
            String property = violation.getPropertyPath().toString();
            String violationMessage = violation.getMessage();

            if ("email.value".equals(property) || "email".equals(property)) {
                bindingResult.rejectValue("email", "EM", violationMessage);
            } else if ("visiblePassword".equals(property)) {
                PasswordRequirements requirements = passwordService.currentRequirements();
                String formattedMessage = MessageFormat.format(violationMessage,
                        requirements.getMinimumLength(), requirements.getMaximumLength()
                );
                bindingResult.rejectValue("newPassword", "NP", formattedMessage);
            }
        }
    }
}
