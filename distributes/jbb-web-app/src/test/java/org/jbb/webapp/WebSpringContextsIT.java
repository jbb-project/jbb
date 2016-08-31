/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp;

import org.jbb.lib.test.CleanHsqlDbAfterTestsConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfigMocks.class, CleanHsqlDbAfterTestsConfig.class,
        LibsCompositeConfig.class, DomainCompositeConfig.class})
@WebAppConfiguration
public class WebSpringContextsIT {
    @Autowired
    private ApplicationContext context;

    @Test
    public void shouldSetContext() throws Exception {
        // then
        assertThat(context).isNotNull();
    }
}
