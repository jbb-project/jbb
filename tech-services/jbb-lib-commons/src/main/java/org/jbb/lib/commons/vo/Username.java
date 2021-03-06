/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.commons.vo;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
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
public class Username implements Serializable {
    @NotEmpty
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[^\\s]+$", message = "{org.jbb.lib.commons.vo.Username.nowhitespace.message}") // no whitespace
            String value;

    @Tolerate
    Username() {
        // for JPA
    }

    public static Username of(String value) {
        return Username.builder().value(value).build();
    }

    @Override
    public String toString() {
        return value;
    }
}
