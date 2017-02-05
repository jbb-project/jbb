/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.acp.form;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLockServiceSettingsForm {

    @Min(value = 1, message = "Maximum of invalid sign in attempts must be greater or equal than one")
    private int maximumNumberOfInvalidSignInAttempts;

    @Min(value = 1, message = "Measurement time period must be greater or equal than one")
    private Long invalidAttemptsMeasurementTimePeriod;

    @Min(value = 1, message = "Account lock time must be greater or equal than one")
    private Long accountLockTimePeriod;

    private boolean serviceAvailable;
}
