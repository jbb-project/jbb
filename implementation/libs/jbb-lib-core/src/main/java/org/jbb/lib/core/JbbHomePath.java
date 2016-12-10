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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class JbbHomePath {
    public static final String JBB_PATH_KEY = "jbb.home";
    private static final String DEFAULT_JBB_PATH = System.getProperty("user.home") + "/jbb";
    private static final String ENV_JBB_PATH = System.getenv("JBB_HOME");

    private Optional<String> jndiJbbHomePath;

    public JbbHomePath(String jndiJbbHomePath) {
        this.jndiJbbHomePath = Optional.ofNullable(jndiJbbHomePath);
    }

    private static void setSystemProperty(String jbbPath) {
        System.setProperty(JBB_PATH_KEY, jbbPath);
    }

    public String getEffective() {
        return System.getProperty(JBB_PATH_KEY);
    }

    void resolveEffectiveAndStoreToSystemProperty() {
        if (jndiJbbHomePath.isPresent()) {
            setSystemProperty(jndiJbbHomePath.get());
        } else if (StringUtils.isNotEmpty(ENV_JBB_PATH)) {
            setSystemProperty(ENV_JBB_PATH);
        } else {
            setSystemProperty(DEFAULT_JBB_PATH);
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
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
