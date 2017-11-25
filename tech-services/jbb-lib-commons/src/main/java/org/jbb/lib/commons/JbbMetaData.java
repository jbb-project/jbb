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


import static org.jbb.lib.commons.PropertiesUtils.buildPropertiesConfiguration;

import java.io.File;
import java.io.IOException;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.springframework.core.io.ClassPathResource;

public class JbbMetaData {
    private static final String MANIFEST_FILENAME = "manifest.data";

    private static final String JBB_VER_KEY = "jbb.version";

    private static final String CONFIG_SUBDIRECTORY = "config";

    private Configuration data;

    private JbbHomePath jbbHomePath;

    public JbbMetaData(JbbHomePath jbbHomePath) {
        this.jbbHomePath = jbbHomePath;
        ClassPathResource manifestDataFile = new ClassPathResource(MANIFEST_FILENAME);
        bindFileToConfiguration(manifestDataFile);
    }

    private void bindFileToConfiguration(ClassPathResource manifestDataFile) {
        try {
            data = buildPropertiesConfiguration(manifestDataFile.getURL());
        } catch (ConfigurationException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public String jbbVersion() {
        return (String) data.getProperty(JBB_VER_KEY);
    }

    public String jbbHomePath() {
        return jbbHomePath.getEffective();
    }

    public String jbbConfigDirectory() {
        return jbbHomePath() + File.separator + CONFIG_SUBDIRECTORY;
    }
}
