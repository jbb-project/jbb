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

import org.jbb.lib.test.PojoTest;
import org.jbb.system.api.logging.model.LogFileAppender;
import org.jbb.system.api.logging.model.LogLevel;
import org.jbb.system.api.logging.model.LogThresholdFilter;
import org.junit.Test;

public class FileAppenderRowTest extends PojoTest {

    @Override
    public Class getClassUnderTest() {
        return FileAppenderRow.class;
    }

    @Test
    public void allArgsConstructorTest() throws Exception {
        // when
        new FileAppenderRow("name", "test.log", "%d-test.log", LogFileAppender.FileSize.valueOf("100 MB"),
                7, new LogThresholdFilter(LogLevel.DEBUG), "%d");

    }
}