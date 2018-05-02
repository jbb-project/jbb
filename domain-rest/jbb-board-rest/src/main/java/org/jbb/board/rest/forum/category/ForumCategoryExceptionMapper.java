/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.rest.forum.category;

import org.jbb.lib.restful.error.ErrorDetail;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ForumCategoryExceptionMapper {

    public ErrorDetail mapToErrorDetail(ConstraintViolation<?> violation) {
        String propertyPath = violation.getPropertyPath().toString();
        return new ErrorDetail(propertyPath, violation.getMessage());
    }
}
