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

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.jbb.lib.core.CoreConfig;
import org.jbb.lib.core.JbbMetaData;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CoreConfigWithoutJndiMocks;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PropertyEncryptionEnableTestConfig.class, PropertiesConfig.class, CoreConfig.class, CoreConfigWithoutJndiMocks.class})
public class PropertiesEncryptionIT {
    @Autowired
    private PropertyEncryptionEnableTestConfig.ExampleProperties exampleConfig;

    @Autowired
    private PropertiesEncryption propertiesEncryption;

    @Autowired
    private JbbMetaData jbbMetaData;

    private PropertiesConfiguration propFile;

    @Test
    public void encryptionAndDecryptionScenario() throws Exception {
        // given
        propertiesEncryption.reconfigureEncryption();

        // when
        // FIRST: PROPERTIES ARE NOT ENCRYPTED
        exampleConfig.setProperty("foo", "not secret");
        exampleConfig.setProperty("bar", "55");
        readPropertiesFile();

        // then
        assertThat(exampleConfig.foo()).isEqualTo("not secret");
        assertThat(exampleConfig.bar()).isEqualTo(55);
        assertThat(propFile.getString("foo")).isEqualTo("not secret");
        assertThat(propFile.getString("bar")).isEqualTo("55");

        // when
        // SECOND: TRIGGER OF PROPERTIES ENCRYPTION USING ENC(...) PLACEHOLDER
        exampleConfig.setProperty("foo", "ENC(secret)");
        exampleConfig.setProperty("bar", "ENC(811)");
        readPropertiesFile();

        // then
        assertThat(exampleConfig.foo()).isEqualTo("secret");
        assertThat(exampleConfig.bar()).isEqualTo(811);
        assertThat(propFile.getString("foo")).startsWith("DEC(").endsWith(")");
        assertThat(propFile.getString("bar")).startsWith("DEC(").endsWith(")");

        // when
        // THIRD: PROPERTIES ARE IN A PUBLIC WAY BUT APPLICATION IS STILL SAVE ENCRYPT VALUES TO FILE
        exampleConfig.setProperty("foo", "another secret");
        exampleConfig.setProperty("bar", "71");
        readPropertiesFile();

        // then
        assertThat(exampleConfig.foo()).isEqualTo("another secret");
        assertThat(exampleConfig.bar()).isEqualTo(71);
        assertThat(propFile.getString("foo")).startsWith("DEC(").endsWith(")");
        assertThat(propFile.getString("bar")).startsWith("DEC(").endsWith(")");
    }

    private void readPropertiesFile() throws ConfigurationException {
        String propertiesFilePath = jbbMetaData.jbbHomePath() + File.separator + "jbb-testbed.properties";
        propFile = new PropertiesConfiguration(propertiesFilePath);
    }
}
