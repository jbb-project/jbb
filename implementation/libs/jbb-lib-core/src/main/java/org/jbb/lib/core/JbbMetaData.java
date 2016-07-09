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

import com.google.common.base.Throwables;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class JbbMetaData {
    private static final String MANIFEST_FILENAME = "manifest.data";

    private static final String JBB_VER_KEY = "jbb.version";

    private Configuration data;

    private JbbHomePath jbbHomePath;

    public JbbMetaData(JbbHomePath jbbHomePath) {
        this.jbbHomePath = jbbHomePath;
        ClassPathResource manifestDataFile = new ClassPathResource(MANIFEST_FILENAME);
        bindFileToConfiguration(manifestDataFile);
    }

    private void bindFileToConfiguration(ClassPathResource manifestDataFile) {
        try {
            data = new PropertiesConfiguration(manifestDataFile.getURL());
        } catch (ConfigurationException | IOException e) {
            Throwables.propagate(e);
        }
    }

    public String jbbVersion() {
        return (String) data.getProperty(JBB_VER_KEY);
    }

    public String jbbHomePath() {
        return jbbHomePath.getEffective();
    }
}
