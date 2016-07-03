/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.api.model;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.Tolerate;

@Value
@Builder
public class Email implements Serializable {
    @NonFinal
    @org.hibernate.validator.constraints.Email
    @NotEmpty
    String value;

    @Tolerate
    Email() {
        // for JPA
    }
}
