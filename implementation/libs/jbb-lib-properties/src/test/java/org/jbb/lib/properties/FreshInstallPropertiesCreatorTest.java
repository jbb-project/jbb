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

import com.google.common.collect.Sets;

import org.aeonbits.owner.Config.Sources;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FreshInstallPropertiesCreatorTest {
    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Mock
    private JbbPropertyFilesResolver propertyFilesResolverMock;

    @InjectMocks
    private FreshInstallPropertiesCreator propertiesCreator;

    @Before
    public void setUp() throws Exception {
        assertThat(propertyFilesResolverMock).isNotNull();
        assertThat(propertiesCreator).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenNullPassed() throws Exception {
        // given
        Class<? extends ModuleProperties> nullClass = null;

        // when
        propertiesCreator.putDefaultPropertiesIfNeeded(nullClass);

        // then
        // throw IllegalArgumentException
    }

    @Test
    public void shouldDoNothing_whenEmptySetPassed() throws Exception {
        // given
        when(propertyFilesResolverMock.resolvePropertyFileNames(ClasspathProperties.class)).thenReturn(Sets.newHashSet());

        // when
        propertiesCreator.putDefaultPropertiesIfNeeded(ClasspathProperties.class);

        // then
        // nothing...
    }

    @Test
    public void shouldCopyDefaultPropertyFiles_whenPropertyFilesDoNotExists() throws Exception {
        // given
        File tempFolder = temp.newFolder();
        File firstPropertyFile = new File(tempFolder.getAbsolutePath() + "/test1.properties");
        File secondPropertyFile = new File(tempFolder.getAbsolutePath() + "/test2.properties");

        assertThat(firstPropertyFile).doesNotExist();
        assertThat(secondPropertyFile).doesNotExist();

        ClassPathResource defaultFirstPropertyFile = new ClassPathResource("test1.properties");
        ClassPathResource defaultSecondPropertyFile = new ClassPathResource("test2.properties");

        when(propertyFilesResolverMock.resolvePropertyFileNames(TestProperties.class)).thenReturn(Sets.newHashSet(
                firstPropertyFile.getAbsolutePath(), secondPropertyFile.getAbsolutePath()
        ));

        // when
        propertiesCreator.putDefaultPropertiesIfNeeded(TestProperties.class);

        // then
        assertThat(firstPropertyFile).exists();
        assertThat(secondPropertyFile).exists();

        assertThat(FileUtils.contentEquals(firstPropertyFile, defaultFirstPropertyFile.getFile())).isTrue();
        assertThat(FileUtils.contentEquals(secondPropertyFile, defaultSecondPropertyFile.getFile())).isTrue();
    }

    @Test
    public void shouldPropagateFileNotFoundException_whenThereIsNoDefaultPropertyFileOnClasspath() throws Exception {
        // given
        File tempFolder = temp.newFolder();
        File notExistPropertyFile = new File(tempFolder.getAbsolutePath() + "/notexist.properties");
        assertThat(notExistPropertyFile).doesNotExist();

        when(propertyFilesResolverMock.resolvePropertyFileNames(TestProperties.class)).thenReturn(Sets.newHashSet(
                notExistPropertyFile.getAbsolutePath()));

        // when
        try {
            propertiesCreator.putDefaultPropertiesIfNeeded(TestProperties.class);
        } catch (RuntimeException e) {
            // then
            assertThat(e).hasRootCauseExactlyInstanceOf(FileNotFoundException.class);
        }
    }

    @Sources({"classpath:test.properties"})
    private interface ClasspathProperties extends ModuleProperties {

        String foo();

        String bar();
    }

    @Sources({"file:${jbb.home}/test1.properties", "file:${jbb.home}/test2.properties"})
    private interface TestProperties extends ModuleProperties {

        String foo();

        String bar();
    }

    @Sources({"classpath:notexist.properties"})
    private interface ClasspathNotExistingProperties extends ModuleProperties {

        String foo();

        String bar();
    }


}