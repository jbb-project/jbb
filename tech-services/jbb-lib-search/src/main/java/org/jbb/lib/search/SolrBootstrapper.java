/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.search;

import org.apache.commons.io.FileUtils;
import org.jbb.lib.commons.JbbMetaData;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;

@Component("solrBootstrapper")
@RequiredArgsConstructor
public class SolrBootstrapper {

    private static final String CLASSPATH_SOLR_CONF_FILE_NAME = "solr.xml";

    private final JbbMetaData jbbMetaData;
    private final SolrDocumentInitializer documentInitializer;

    @PostConstruct
    public void configure() {
        prepareSolrDirectory();
        prepareConfigurationFile();
        documentInitializer.initDocuments();
    }

    public String getSolrPath() {
        return jbbMetaData.jbbSolrDirectory();
    }

    private void prepareSolrDirectory() {
        String solrDirPath = jbbMetaData.jbbSolrDirectory();

        File solrDir = new File(solrDirPath);
        if (!solrDir.exists()) {
            solrDir.mkdir();
        }
    }

    private String prepareConfigurationFile() {
        String logConfFilePath = jbbMetaData.jbbSolrDirectory() + File.separator + CLASSPATH_SOLR_CONF_FILE_NAME;
        File solrConfigurationFile = new File(logConfFilePath);
        if (!solrConfigurationFile.exists()) {
            copyDefaultConfigurationToFile(solrConfigurationFile);
        }
        return logConfFilePath;
    }

    private void copyDefaultConfigurationToFile(File solrConfigurationFile) {
        ClassPathResource classPathResource = new ClassPathResource(CLASSPATH_SOLR_CONF_FILE_NAME);
        try {
            FileUtils.copyURLToFile(classPathResource.getURL(), solrConfigurationFile);
        } catch (IOException e) {
            throw new IllegalStateException(
                "Unexpected error during reading default solr configuration file", e);
        }
    }

}
