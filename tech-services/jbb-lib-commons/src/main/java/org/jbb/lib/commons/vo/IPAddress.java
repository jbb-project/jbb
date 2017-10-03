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


@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "value")
public class IPAddress implements Serializable {

    @NotEmpty
    String value;

    IPAddress() {
        // for JPA
    }

    @Override
    public String toString() {
        return value;
    }
}
