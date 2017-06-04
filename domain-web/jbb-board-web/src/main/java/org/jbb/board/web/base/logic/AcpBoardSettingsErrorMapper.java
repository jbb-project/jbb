/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.web.base.logic;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.Set;

import javax.validation.ConstraintViolation;

@Component
public class AcpBoardSettingsErrorMapper {
    public void map(Set<ConstraintViolation<?>> constraintViolations, BindingResult bindingResult) {
        for (ConstraintViolation violation : constraintViolations) {
            String property = violation.getPropertyPath().toString();
            String violationMessage = violation.getMessage();

            if ("boardName".equals(property)) {
                bindingResult.rejectValue("boardName", "BM", violationMessage);
            } else if ("dateFormat".equals(property)) {
                bindingResult.rejectValue("dateFormat", "DF", violationMessage);
            }
        }
    }
}
