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
import org.reflections.Reflections;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.solr.core.mapping.SolrDocument;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import lombok.RequiredArgsConstructor;

import static java.io.File.separator;

@Component
@RequiredArgsConstructor
public class SolrDocumentInitializer {
    private final Reflections reflections = new Reflections("org.jbb");
    private final JbbMetaData jbbMetaData;

    public void initDocuments() {
        getSolrDocumentClasses().forEach(this::init);
    }

    private Set<Class<?>> getSolrDocumentClasses() {
        return reflections.getTypesAnnotatedWith(SolrDocument.class);
    }

    private void init(Class<?> documentClass) {
        SolrDocument solrDocument = documentClass.getAnnotation(SolrDocument.class);
        init(solrDocument.collection());
    }

    private void init(String solrCoreName) {
        String solrDir = jbbMetaData.jbbSolrDirectory();
        File solrCoreDir = new File(solrDir + separator + solrCoreName);
        if (!solrCoreDir.exists()) {
            solrCoreDir.mkdir();
        }
        copyDefaultFileIfApplicable(solrCoreName, "core.properties");
        copyDefaultFileIfApplicable(solrCoreName, "schema.xml");
        copyDefaultFileIfApplicable(solrCoreName, "solrconfig.xml");
    }

    private void copyDefaultFileIfApplicable(String solrCoreName, String file) {
        String solrDir = jbbMetaData.jbbSolrDirectory();
        File targetFile = new File(solrDir + separator + solrCoreName + separator + file);
        File targetBakFile = new File(solrDir + separator + solrCoreName + separator + file + ".bak");
        if (targetFile.exists() || targetBakFile.exists()) {
            return;
        }
        ClassPathResource classPathResource = new ClassPathResource("solr" + separator + solrCoreName + separator + file);
        try {
            FileUtils.copyURLToFile(classPathResource.getURL(), targetFile);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unexpected error during reading default solr configuration file", e);
        }
    }

}
