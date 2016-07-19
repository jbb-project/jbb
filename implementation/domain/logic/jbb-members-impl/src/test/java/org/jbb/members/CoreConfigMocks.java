/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members;

import org.jbb.lib.core.JbbMetaData;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import static org.mockito.Mockito.when;

@Configuration
@Slf4j
public class CoreConfigMocks {
    @Autowired
    private ApplicationContext context;

    public void recursiveDeleteOnShutdownHook(final Path path) {
        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    try {
                        try {
                            DataSource dataSource = context.getBean(DataSource.class);
                            dataSource.getConnection().createStatement().execute("SHUTDOWN");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (IllegalStateException e) {
                            // ignore...
                        }
                        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                            @Override
                            public FileVisitResult visitFile(Path file,
                                                             @SuppressWarnings("unused") BasicFileAttributes attrs)
                                    throws IOException {
                                Files.delete(file);
                                return FileVisitResult.CONTINUE;
                            }

                            @Override
                            public FileVisitResult postVisitDirectory(Path dir, IOException e)
                                    throws IOException {
                                if (e == null) {
                                    Files.delete(dir);
                                    return FileVisitResult.CONTINUE;
                                }
                                // directory iteration failed
                                throw e;
                            }
                        });
                    } catch (IOException e) {
                        log.warn("Failed to delete {}", path, e);
                    }
                }));
    }

    @Bean
    @Primary
    public JbbMetaData jbbMetaData() {
        JbbMetaData metaDataMock = Mockito.mock(JbbMetaData.class);
        File tempDir = com.google.common.io.Files.createTempDir();
        recursiveDeleteOnShutdownHook(tempDir.toPath());
        when(metaDataMock.jbbHomePath()).thenReturn(tempDir.getAbsolutePath());
        System.setProperty("jbb.home", tempDir.getAbsolutePath());
        return metaDataMock;
    }
}
