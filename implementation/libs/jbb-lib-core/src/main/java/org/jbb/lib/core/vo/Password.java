/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.core.vo;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public final class Password implements Serializable {
    char[] value;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Password password = (Password) o;
        return Arrays.equals(getValue(), password.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}
