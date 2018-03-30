/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.session.model;

import org.junit.Test;
import org.meanbean.test.BeanTester;

import java.time.Duration;
import java.time.LocalDateTime;

public class SessionImplTest {

    @Test
    public void pojoTest() {
        BeanTester beanTester = new BeanTester();
        beanTester.setIterations(3);
        beanTester.getFactoryCollection().addFactory(LocalDateTime.class, () -> LocalDateTime.now());
        beanTester.getFactoryCollection().addFactory(Duration.class, () -> Duration.ofMinutes(1L));

        beanTester.testBean(SessionImpl.class);
    }

}