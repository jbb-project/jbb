/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.test;

import org.jbb.lib.core.JbbMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class CleanHsqlDbAfterTestsConfig {
    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void postConstruct() {
        JbbMetaData jbbMetaData = context.getBean(JbbMetaData.class);
        DataSource dataSource = context.getBean(DataSource.class);

        recursiveDeleteOnShutdownHook(Paths.get(jbbMetaData.jbbHomePath()), dataSource);
    }


    public void recursiveDeleteOnShutdownHook(final Path path, DataSource dataSource) {
        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    try {
                        try {
                            dataSource.getConnection().createStatement().execute("SHUTDOWN");
                        } catch (SQLException e) {
                            log.warn("SQL Error", e);
                        } catch (IllegalStateException e) {
                            log.debug("Error", e);
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
}
