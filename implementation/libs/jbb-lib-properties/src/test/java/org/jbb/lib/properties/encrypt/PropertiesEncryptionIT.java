/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties.encrypt;

import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PropertyEncryptionEnableTestConfig.class, PropertiesConfig.class, CoreConfigMocks.class})
public class PropertiesEncryptionIT {//todo
    @Autowired
    private PropertyEncryptionEnableTestConfig.ExampleProperties exampleConfig;

    @Autowired
    private PropertiesEncryption propertiesEncryption;

    @Test
    public void name() throws Exception {
        // given
        propertiesEncryption.reconfigureEncryption();

        // when
        exampleConfig.setProperty("foo", "not secret");
        exampleConfig.setProperty("bar", "55");

        // then
        assertThat(exampleConfig.foo()).isEqualTo("not secret");
        assertThat(exampleConfig.bar()).isEqualTo(55);

        // when
        exampleConfig.setProperty("foo", "ENC(secret)");
        exampleConfig.setProperty("bar", "ENC(811)");

        // then
        //check in file...
        assertThat(exampleConfig.foo()).isEqualTo("secret");
        assertThat(exampleConfig.bar()).isEqualTo(811);

        // when
        exampleConfig.setProperty("foo", "another secret");
        exampleConfig.setProperty("bar", "71");

        // then
        //check in file...
        assertThat(exampleConfig.foo()).isEqualTo("another secret");
        assertThat(exampleConfig.bar()).isEqualTo(71);
    }
}
