/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties.encrypt;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyTypeCastingTest {
    private PropertyTypeCasting propertyTypeCasting;

    @Before
    public void setUp() throws Exception {
        propertyTypeCasting = new PropertyTypeCasting();
    }

    @Test
    public void castString() throws Exception {
        // when
        Object result = propertyTypeCasting.resolve("foo", String.class);

        // then
        assertThat(result).isExactlyInstanceOf(String.class);
        assertThat(result).isEqualTo("foo");
    }

    @Test
    public void castBoolean() throws Exception {
        // when
        Object result = propertyTypeCasting.resolve("true", Boolean.class);

        // then
        assertThat(result).isExactlyInstanceOf(Boolean.class);
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void castInt() throws Exception {
        // when
        Object result = propertyTypeCasting.resolve("42", Integer.class);

        // then
        assertThat(result).isExactlyInstanceOf(Integer.class);
        assertThat(result).isEqualTo(42);
    }

    @Test
    public void castLong() throws Exception {
        // when
        Object result = propertyTypeCasting.resolve("11838237432", Long.class);

        // then
        assertThat(result).isExactlyInstanceOf(Long.class);
        assertThat(result).isEqualTo(11838237432L);
    }
}