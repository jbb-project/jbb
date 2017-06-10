/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.cache;

import com.hazelcast.config.Config;
import com.hazelcast.config.FileSystemXmlConfig;

import org.apache.commons.io.FileUtils;
import org.jbb.lib.commons.JbbMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.PostConstruct;

@Component
class HazelcastConfigFilesManager {
    private static final String HAZELCAST_CLIENT_CONFIG_NAME = "hazelcast-client.xml";
    private static final String HAZELCAST_SERVER_CONFIG_NAME = "hazelcast.xml";

    private final JbbMetaData jbbMetaData;

    @Autowired
    public HazelcastConfigFilesManager(JbbMetaData jbbMetaData) {
        this.jbbMetaData = jbbMetaData;
    }

    @PostConstruct
    public void putDefaultHazelcastConfigsIfNeeded() {
        copyFromClasspath(HAZELCAST_CLIENT_CONFIG_NAME);
        copyFromClasspath(HAZELCAST_SERVER_CONFIG_NAME);
    }

    public Config getHazelcastServerConfig() {
        try {
            return new FileSystemXmlConfig(jbbMetaData.jbbHomePath() + File.separator + HAZELCAST_SERVER_CONFIG_NAME);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private void copyFromClasspath(String classpathFileName) {

        ClassPathResource classPathResource = new ClassPathResource(classpathFileName);
        File targetFile = new File(jbbMetaData.jbbHomePath() + File.separator + classpathFileName);
        try {
            if (!targetFile.exists()) {
                FileUtils.copyURLToFile(classPathResource.getURL(), targetFile);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
