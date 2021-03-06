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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
class JbbHomePath {
    public static final String JNDI_NAME = "jbb/home";
    public static final String JBB_PATH_KEY = "jbb.home";

    private static final String DEFAULT_JBB_PATH = System.getProperty("user.home") + "/jbb";
    private static final String ENV_JBB_PATH = System.getenv("JBB_HOME");

    protected static String effectiveJbbHomePath;

    private Optional<String> jndiJbbHomePath;

    public JbbHomePath(JndiValueReader jndiValueReader) {
        this.jndiJbbHomePath = Optional.ofNullable(jndiValueReader.readValue(JNDI_NAME));
    }

    @PostConstruct
    void setUp() {
        resolveEffective();
        createIfNotExists();
    }

    private static void storeEffectivePath(String jbbPath) {
        effectiveJbbHomePath = jbbPath;
        System.setProperty(JBB_PATH_KEY, jbbPath);
    }

    public String getEffective() {
        return effectiveJbbHomePath;
    }

    void resolveEffective() {
        if (jndiJbbHomePath.isPresent()) {
            storeEffectivePath(jndiJbbHomePath.get());
        } else if (StringUtils.isNotEmpty(ENV_JBB_PATH)) {
            storeEffectivePath(ENV_JBB_PATH);
        } else {
            storeEffectivePath(DEFAULT_JBB_PATH);
        }
        log.info("Resolved jBB home path: {}", getEffective());
    }

    void createIfNotExists() {
        try {
            Path jbbPath = Paths.get(getEffective());
            if (!jbbPath.toFile().exists()) {
                Files.createDirectory(jbbPath);
            }
            Validate.isTrue(jbbPath.toFile().isDirectory(), String.format("%s is not a directory!", jbbPath));

            Path configJbbPath = Paths.get(jbbPath.toFile().getAbsolutePath(), "config");
            if (!configJbbPath.toFile().exists()) {
                Files.createDirectory(configJbbPath);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
