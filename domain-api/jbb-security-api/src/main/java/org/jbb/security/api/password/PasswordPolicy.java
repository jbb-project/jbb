/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.api.password;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jbb.security.api.password.validation.MinimumLessOrEqualToMaximum;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@MinimumLessOrEqualToMaximum
public class PasswordPolicy {

    @Min(1)
    @NotNull
    private Integer minimumLength;

    @Min(1)
    @NotNull
    private Integer maximumLength;

}
