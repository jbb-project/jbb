/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties;

import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.properties.PropertyAspectEnableTestConfig.BlankProperties;
import org.jbb.lib.test.MockWithoutJndiCommonsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PropertyAspectEnableTestConfig.class, PropertiesConfig.class, CommonsConfig.class, MockWithoutJndiCommonsConfig.class})
public class SafeBlankPropertyAspectIT {
    @Autowired
    private BlankProperties blankConfig;

    @Test
    public void shouldUseValuesFromFileOnClasspath() throws Exception {
        // then
        assertThat(blankConfig.blankString()).isNull();
        assertThat(blankConfig.blankInteger()).isNull();
        assertThat(blankConfig.blankLong()).isNull();
        assertThat(blankConfig.blankBoolean()).isNull();

        assertThat(blankConfig.emptyString()).isNull();
        assertThat(blankConfig.emptyInteger()).isNull();
        assertThat(blankConfig.emptyLong()).isNull();
        assertThat(blankConfig.emptyBoolean()).isNull();
    }


}