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

import com.google.common.collect.Lists;

import org.jbb.system.api.model.logging.LogLevel;
import org.junit.Test;
import org.meanbean.test.BeanTester;

public class LoggerRowTest {
    @Test
    public void pojoTest() throws Exception {
        BeanTester beanTester = new BeanTester();
        beanTester.setIterations(3);

        beanTester.testBean(LoggerRow.class);
    }

    @Test
    public void allArgsConstructorTest() throws Exception {
        // when
        new LoggerRow("name", LogLevel.ALL, true, Lists.newArrayList());
    }
}