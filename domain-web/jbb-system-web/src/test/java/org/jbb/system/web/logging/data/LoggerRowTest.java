/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.logging.data;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Lists;
import org.jbb.lib.test.PojoTest;
import org.jbb.system.api.logging.model.LogLevel;
import org.junit.Test;

public class LoggerRowTest extends PojoTest {

    @Override
    public Class getClassUnderTest() {
        return LoggerRow.class;
    }

    @Test
    public void addivityPositiveTest() throws Exception {
        // when
        LoggerRow firstRow = new LoggerRow("name", LogLevel.ALL, true, Lists.newArrayList());

        // then
        assertThat(firstRow.getAddivity()).isEqualTo("true");
    }

    @Test
    public void addivityNegativeTest() throws Exception {
        // when
        LoggerRow firstRow = new LoggerRow("name", LogLevel.ALL, false, Lists.newArrayList());

        // then
        assertThat(firstRow.getAddivity()).isEqualTo("false");
    }

    @Test
    public void addivityRootTest() throws Exception {
        // when
        LoggerRow firstRow = new LoggerRow("ROOT", LogLevel.ALL, true, Lists.newArrayList());

        // then
        assertThat(firstRow.getAddivity()).isEqualTo("N/A");
    }
}