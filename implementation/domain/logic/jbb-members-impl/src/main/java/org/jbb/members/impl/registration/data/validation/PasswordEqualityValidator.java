/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.registration.data.validation;

import org.jbb.members.impl.registration.data.PasswordPair;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordEqualityValidator implements ConstraintValidator<PasswordEquality, PasswordPair> {
    @Override
    public void initialize(PasswordEquality constraintAnnotation) {
        // not needed...
    }

    @Override
    public boolean isValid(PasswordPair pair, ConstraintValidatorContext context) {
        return pair.getPassword().equals(pair.getPasswordAgain());
    }

}
