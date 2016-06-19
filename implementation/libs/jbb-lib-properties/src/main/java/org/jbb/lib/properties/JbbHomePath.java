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

import com.google.common.base.Throwables;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public final class JbbHomePath {
    private static final String DEFAULT_JBB_HOME_PATH = System.getProperty("user.home") + "/jbb";
    private static final String ENV_JBB_HOME_PATH = System.getenv("JBB_HOME");
    private static final String EFFECTIVE_JBB_HOME_PATH_KEY = "jbb.home";

    private JbbHomePath() {
        // util class..
    }

    public static void resolveEffectiveAndStoreToSystemProperty() {
        //TODO check in JNDI first
        if (StringUtils.isNotEmpty(ENV_JBB_HOME_PATH)) {
            setSystemProperty(ENV_JBB_HOME_PATH);
        } else {
            setSystemProperty(DEFAULT_JBB_HOME_PATH);
        }
    }

    private static void setSystemProperty(String jbbPath) {
        System.setProperty(EFFECTIVE_JBB_HOME_PATH_KEY, jbbPath);
    }

    public static String getEffective() {
        return System.getProperty(EFFECTIVE_JBB_HOME_PATH_KEY);
    }

    public static void createIfNotExists() {
        try {
            Path jbbPath = Paths.get(JbbHomePath.getEffective());
            if (Files.notExists(jbbPath)) {
                Files.createDirectory(jbbPath);
            }
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }
}
