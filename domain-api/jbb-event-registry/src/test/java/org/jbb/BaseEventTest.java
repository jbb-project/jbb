/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb;

import com.google.common.eventbus.EventBus;

import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EventBusConfig.class, CommonsConfig.class,
        MockCommonsConfig.class})
public abstract class BaseEventTest {

    @Autowired
    protected EventBus eventBus;

}
