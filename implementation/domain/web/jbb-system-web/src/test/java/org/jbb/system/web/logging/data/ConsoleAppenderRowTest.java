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

import org.jbb.system.api.model.logging.LogConsoleAppender;
import org.jbb.system.api.model.logging.LogLevel;
import org.jbb.system.api.model.logging.LogThresholdFilter;
import org.junit.Test;
import org.meanbean.test.BeanTester;

public class ConsoleAppenderRowTest {
    @Test
    public void pojoTest() throws Exception {
        BeanTester beanTester = new BeanTester();
        beanTester.setIterations(3);

        beanTester.testBean(ConsoleAppenderRow.class);
    }

    @Test
    public void allArgsConstructorTest() throws Exception {
        // when
        new ConsoleAppenderRow("name", LogConsoleAppender.Target.SYSTEM_OUT, new LogThresholdFilter(LogLevel.DEBUG),
                "%d", true);

    }
}