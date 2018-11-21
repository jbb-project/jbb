/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.logging.model;


import com.google.common.collect.Lists;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class AppLoggerTest {

    @Test
    public void pojoTest() {
        AppLogger logger = new AppLogger();

        logger.setName("foo");
        assertThat(logger.getName()).isEqualTo("foo");

        logger.setLevel(LogLevel.ERROR);
        assertThat(logger.getLevel()).isEqualTo(LogLevel.ERROR);

        logger.setAddivity(true);
        assertThat(logger.isAddivity()).isTrue();

        logger.setAppenders(Lists.newArrayList(mock(LogAppender.class)));
        assertThat(logger.getAppenders()).isNotEmpty();
    }

    @Test
    public void testRootName() {
        AppLogger logger = new AppLogger();

        logger.setName("ROOT");
        assertThat(logger.isRootLogger()).isTrue();

        logger.setName("Root");
        assertThat(logger.isRootLogger()).isTrue();

        logger.setName("root");
        assertThat(logger.isRootLogger()).isTrue();

        logger.setName("ROOT.subpackage");
        assertThat(logger.isRootLogger()).isFalse();

        logger.setName("foo");
        assertThat(logger.isRootLogger()).isFalse();
    }
}