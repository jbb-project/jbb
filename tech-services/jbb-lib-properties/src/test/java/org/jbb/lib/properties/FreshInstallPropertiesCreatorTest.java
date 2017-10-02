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

import com.google.common.collect.Sets;

import org.aeonbits.owner.Config.Sources;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullPassed() throws Exception {
        // given
        Class<? extends ModuleProperties> nullClass = null;

        // when
        propertiesCreator.putDefaultPropertiesIfNeeded(nullClass);

        // then
        // throw NullPointerException
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

        assertThat(firstPropertyFile).hasSameContentAs(defaultFirstPropertyFile.getFile());
        assertThat(secondPropertyFile).hasSameContentAs(defaultSecondPropertyFile.getFile());
    }

    @Test
    public void shouldAddMissingPropertiesToTarget_whenPropertyFilesExists_butIsNotComplete() throws Exception {
        // given
        File tempFolder = temp.newFolder();
        File targetPropertyFile = new File(tempFolder.getAbsolutePath() + "/test3.properties");
        FileUtils.copyURLToFile(new ClassPathResource("test3-missing.properties").getURL(), targetPropertyFile);
        ClassPathResource referencePropertyFile = new ClassPathResource("test3.properties");

        when(propertyFilesResolverMock.resolvePropertyFileNames(TestMissingProperties.class)).thenReturn(Sets.newHashSet(
                targetPropertyFile.getAbsolutePath()
        ));

        // when
        propertiesCreator.putDefaultPropertiesIfNeeded(TestMissingProperties.class);

        // then
        assertThat(targetPropertyFile).exists();
        assertThat(targetPropertyFile).hasSameContentAs(referencePropertyFile.getFile());
    }

    @Test
    public void shouldDeleteObsoletePropertiesToTarget_whenPropertyFilesExists_butHasOldPropertyKeys() throws Exception {
        // given
        File tempFolder = temp.newFolder();
        File targetPropertyFile = new File(tempFolder.getAbsolutePath() + "/test4.properties");
        FileUtils.copyURLToFile(new ClassPathResource("test4-obsolete.properties").getURL(), targetPropertyFile);
        ClassPathResource referencePropertyFile = new ClassPathResource("test4.properties");

        when(propertyFilesResolverMock.resolvePropertyFileNames(TestObsoleteProperties.class)).thenReturn(Sets.newHashSet(
                targetPropertyFile.getAbsolutePath()
        ));

        // when
        propertiesCreator.putDefaultPropertiesIfNeeded(TestObsoleteProperties.class);

        // then
        assertThat(targetPropertyFile).exists();
        assertThat(targetPropertyFile).hasSameContentAs(referencePropertyFile.getFile());
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

    @Sources({"file:${jbb.home}/test3.properties"})
    private interface TestMissingProperties extends ModuleProperties {

        String foo();

        String bar();
    }

    @Sources({"file:${jbb.home}/test4.properties"})
    private interface TestObsoleteProperties extends ModuleProperties {

        String foo();

        String bar();
    }


}