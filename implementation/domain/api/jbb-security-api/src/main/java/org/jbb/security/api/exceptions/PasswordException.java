/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.api.exceptions;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class PasswordException extends ConstraintViolationException {// NOSONAR
    // (This class has 6 parents which is greater than 5 authorized.)

    public PasswordException(Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(constraintViolations);
    }

}
