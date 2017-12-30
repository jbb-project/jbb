/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.commons;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class JbbHomePathCreateTest {
    @Rule
    public TemporaryFolder temp = new TemporaryFolder();
    @Mock
    JndiValueReader jndiValueReaderMock;
    private JbbHomePath jbbHomePath;

    @Before
    public void setUp() throws Exception {
        jbbHomePath = new JbbHomePath(jndiValueReaderMock);
    }

    @Test
    public void shouldCreateJbbHomeFolder_whenItDoesntExists() throws Exception {
        // given
        File tempFolder = temp.newFolder();
        tempFolder.delete();
        assertThat(tempFolder).doesNotExist();

        System.setProperty(JbbHomePath.JBB_PATH_KEY, tempFolder.getAbsolutePath());
        JbbHomePath.effectiveJbbHomePath = tempFolder.getAbsolutePath();

        // when
        jbbHomePath.createIfNotExists();

        // then
        assertThat(tempFolder).exists();
    }

    @Test
    public void shouldDoNothing_whenJbbHomeFolderAlreadyExists() throws Exception {
        // given
        File tempFolder = temp.newFolder();
        assertThat(tempFolder).exists();
        assertThat(tempFolder).isDirectory();
        assertThat(tempFolder.list()).isEmpty();

        System.setProperty(JbbHomePath.JBB_PATH_KEY, tempFolder.getAbsolutePath());
        JbbHomePath.effectiveJbbHomePath = tempFolder.getAbsolutePath();

        // when
        jbbHomePath.createIfNotExists();
        String[] listOfUntouchedFiles = tempFolder.list((dir, name) -> !"config".equals(name));

        // then
        assertThat(tempFolder).exists();
        assertThat(listOfUntouchedFiles).isEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenPathLinksToFile_notToDirectory() throws Exception {
        // given
        File tempFile = temp.newFile();
        assertThat(tempFile).exists();

        System.setProperty(JbbHomePath.JBB_PATH_KEY, tempFile.getAbsolutePath());
        JbbHomePath.effectiveJbbHomePath = tempFile.getAbsolutePath();

        // when
        jbbHomePath.createIfNotExists();

        // then
        // throw IllegalArgumentException
    }
}