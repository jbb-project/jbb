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

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.Tolerate;

@Value
@Builder
public class DisplayedName implements Serializable {
    @NonFinal // JPA
    String value;

    @Tolerate
    DisplayedName() {
        // for JPA
    }
}
