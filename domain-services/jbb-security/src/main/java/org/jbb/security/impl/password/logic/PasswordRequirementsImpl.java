/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password.logic;


import org.jbb.security.api.password.PasswordRequirements;
import org.jbb.security.impl.password.logic.validation.PasswordRequirementsConsistent;

import javax.validation.constraints.Min;

@PasswordRequirementsConsistent
public class PasswordRequirementsImpl implements PasswordRequirements {
    @Min(1)
    private int minimumLength;

    @Min(1)
    private int maximumLength;

    public PasswordRequirementsImpl(PasswordRequirements newRequirements) {
        minimumLength = newRequirements.minimumLength();
        maximumLength = newRequirements.maximumLength();
    }

    @Override
    public int minimumLength() {
        return minimumLength;
    }

    @Override
    public int maximumLength() {
        return maximumLength;
    }
}
