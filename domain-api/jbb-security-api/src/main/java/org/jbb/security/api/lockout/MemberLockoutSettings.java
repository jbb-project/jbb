/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.api.lockout;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLockoutSettings {

    @Min(1)
    @NotNull
    private Integer failedAttemptsThreshold;

    @Min(1)
    @NotNull
    private Long failedSignInAttemptsExpirationMinutes;

    @Min(1)
    @NotNull
    private Long lockoutDurationMinutes;

    private boolean lockingEnabled;

}
