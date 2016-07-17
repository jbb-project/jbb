/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties;

import org.aeonbits.owner.Config;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.jbb.lib.core.JbbMetaData;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import java.beans.PropertyChangeEvent;
import java.io.File;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateFilePropertyChangeListenerTest {
    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Mock
    private JbbMetaData jbbMetaDataMock;

    private File testPropertyFile;

    private UpdateFilePropertyChangeListener listener;

    @Before
    public void setUp() throws Exception {
        // given
        testPropertyFile = temp.newFile("test1.properties");
        FileUtils.copyFile(new ClassPathResource("test1.properties").getFile(), testPropertyFile);

        when(jbbMetaDataMock.jbbHomePath()).thenReturn(testPropertyFile.getParentFile().getAbsolutePath());

        listener = new UpdateFilePropertyChangeListener(new JbbPropertyFilesResolver(jbbMetaDataMock),
                TestProperties.class);
    }

    @Test
    public void shouldUpdateFileContent_whenPropertyChange() throws Exception {
        // given
        // see setUp method

        // when
        listener.propertyChange(new PropertyChangeEvent(testPropertyFile, "foo", "test1", "new"));

        // then
        assertThat(FileUtils.contentEquals(testPropertyFile,
                new ClassPathResource("test1-after-change-event.properties").getFile())).isTrue();
    }

    @Test
    public void shouldPropagateConfigurationException_whenPropertyFileSuddenlyLost() throws Exception {
        // given
        // see setUp method

        // when
        testPropertyFile.delete();

        try {
            listener.propertyChange(new PropertyChangeEvent(testPropertyFile, "foo", "test1", "new"));
        } catch (RuntimeException e) {
            // then
            assertThat(e).hasRootCauseInstanceOf(ConfigurationException.class);
        }
    }

    @Config.Sources({"file:${jbb.home}/test1.properties"})
    private interface TestProperties extends ModuleProperties {

        String foo();
    }
}