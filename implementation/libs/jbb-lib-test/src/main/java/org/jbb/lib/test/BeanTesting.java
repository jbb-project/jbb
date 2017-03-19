/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.test;

import org.junit.Before;
import org.junit.Test;
import org.meanbean.test.BeanTester;

import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;

public class BeanTesting<T> {

    private BeanTester beanTester;
    private Class<T> clazz;

    @Before
    public void before() {
        beanTester = new BeanTester();

        clazz = (Class<T>)
                ((ParameterizedType) getClass()
                        .getGenericSuperclass())
                        .getActualTypeArguments()[0];
    }

    @Test
    public void beanTest() throws Exception {
        BeanTester beanTester = new BeanTester();
        beanTester.setIterations(3);
        beanTester.getFactoryCollection().addFactory(LocalDateTime.class, () -> LocalDateTime.now());

        beanTester.testBean(clazz);
    }

}
