/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.api.base;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Tolerate;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "value")
@Builder
public class DisplayedName implements Serializable {
    @NotEmpty
    @Size(min = 3, max = 64)
    String value;

    @Tolerate
    DisplayedName() {
        // for JPA
    }

    public static DisplayedName of(String value) {
        return DisplayedName.builder()
                .value(value)
                .build();
    }

    @Override
    public String toString() {
        return value;
    }
}
