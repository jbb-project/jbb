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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "value")
public class Email implements Serializable {

    @javax.validation.constraints.Email
    @NotEmpty
    @Length(min = 3, max = 254)
    String value;

    @Tolerate
    Email() {
        // for JPA
    }

    public static Email of(String value) {
        return Email.builder().value(value).build();
    }

    @Override
    public String toString() {
        return value;
    }
}
