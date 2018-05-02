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
import org.jbb.system.api.logging.model.LogConsoleAppender;
import org.jbb.system.api.logging.model.LogLevel;
import org.jbb.system.api.logging.model.LogThresholdFilter;
import org.junit.Test;

public class ConsoleAppenderRowTest extends PojoTest {

    @Override
    public Class getClassUnderTest() {
        return ConsoleAppenderRow.class;
    }

    @Test
    public void allArgsConstructorTest() throws Exception {
        // when
        new ConsoleAppenderRow("name", LogConsoleAppender.Target.SYSTEM_OUT, new LogThresholdFilter(LogLevel.DEBUG),
                "%d", true);

    }
}