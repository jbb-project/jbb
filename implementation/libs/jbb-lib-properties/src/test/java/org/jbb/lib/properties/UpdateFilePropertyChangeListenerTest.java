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
import org.apache.commons.io.FileUtils;
import org.jbb.lib.core.JbbHomePath;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.io.ClassPathResource;

import java.beans.PropertyChangeEvent;
import java.io.File;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JbbHomePath.class)
public class UpdateFilePropertyChangeListenerTest {
    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(JbbHomePath.class);
    }

    @Test
    public void testName() throws Exception {
        // given
        File testPropertyFile = temp.newFile("test1.properties");
        FileUtils.copyFile(new ClassPathResource("test1.properties").getFile(), testPropertyFile);

        when(JbbHomePath.getEffective()).thenReturn(testPropertyFile.getParentFile().getAbsolutePath());

        // when
        UpdateFilePropertyChangeListener listener = new UpdateFilePropertyChangeListener(TestProperties.class);
        listener.propertyChange(new PropertyChangeEvent(testPropertyFile, "foo", "test1", "new"));

        // then
        assertThat(FileUtils.contentEquals(testPropertyFile,
                new ClassPathResource("test1-after-change-event.properties").getFile())).isTrue();
    }

    @Config.Sources({"file:${jbb.home}/test1.properties"})
    private interface TestProperties extends ModuleProperties {

        String foo();
    }
}