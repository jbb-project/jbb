/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.core;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class JbbHomePathCreateTest {
    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private JbbHomePath jbbHomePath;

    @Before
    public void setUp() throws Exception {
        jbbHomePath = new JbbHomePath(Optional.empty());
    }

    @Test
    public void shouldCreateJbbHomeFolder_whenItDoesntExists() throws Exception {
        // given
        File tempFolder = temp.newFolder();
        tempFolder.delete();
        assertThat(tempFolder).doesNotExist();

        System.setProperty(JbbHomePath.JBB_PATH_KEY, tempFolder.getAbsolutePath());

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
        long lastModified = tempFolder.lastModified();

        System.setProperty(JbbHomePath.JBB_PATH_KEY, tempFolder.getAbsolutePath());

        // when
        jbbHomePath.createIfNotExists();

        // then
        assertThat(tempFolder).exists();
        assertThat(tempFolder.lastModified()).isEqualTo(lastModified);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenPathLinksToFile_notToDirectory() throws Exception {
        // given
        File tempFile = temp.newFile();
        assertThat(tempFile).exists();

        System.setProperty(JbbHomePath.JBB_PATH_KEY, tempFile.getAbsolutePath());

        // when
        jbbHomePath.createIfNotExists();

        // then
        // throw IllegalArgumentException
    }
}