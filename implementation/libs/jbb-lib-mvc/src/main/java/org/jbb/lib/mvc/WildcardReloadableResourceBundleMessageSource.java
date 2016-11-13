/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.substringAfterLast;
import static org.apache.commons.lang3.StringUtils.substringBefore;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;
import static org.apache.commons.lang3.StringUtils.substringBetween;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

/**
 * Source: http://springrules.blogspot.com/2014/09/using-wildcards-for-spring.html
 */
@Slf4j
public class WildcardReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {
    private static final String PROPERTIES_SUFFIX = ".properties";

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    @Override
    public void setBasenames(String... baseNames) {
        if (baseNames == null) {
            return;
        }

        for (int i = 0; i < baseNames.length; i++) {
            resolveResourcesForBaseName(baseNames[i]);
        }
    }

    private void resolveResourcesForBaseName(String baseName) {
        String basename = trimToEmpty(baseName);

        if (isNotBlank(basename)) {
            List<String> resultBaseNameList = processResourcesForBaseName(basename);
            String[] resultBaseNamesArray = resultBaseNameList.toArray(new String[]{});
            super.setBasenames(resultBaseNamesArray);
        }
    }

    private List<String> processResourcesForBaseName(String basename) {
        List<String> resultBaseNameList = new ArrayList<>();

        Resource[] resources;
        try {
            resources = resourcePatternResolver.getResources(basename);
        } catch (IOException e) {
            log.debug("No message source files found for basename '{}'", basename, e);
            return resultBaseNameList;
        }

        for (int j = 0; j < resources.length; j++) {
            processResource(resources[j], resultBaseNameList);
        }

        return resultBaseNameList;
    }

    private void processResource(Resource resource, List<String> resultBaseNameList) {
        String uri = null;
        try {
            uri = resource.getURI().toString();
        } catch (IOException e) {
            log.debug("Error when getting URI from resource: {}", resource, e);
            return;
        }

        String baseName = null;
        if (resource instanceof FileSystemResource) {
            baseName = substringBetween(uri, "/classes/", PROPERTIES_SUFFIX);
        } else if (resource instanceof ClassPathResource) {
            baseName = substringBefore(uri, PROPERTIES_SUFFIX);
        } else if (resource instanceof UrlResource) {
            baseName = "classpath:" + substringBetween(uri, ".jar!/", PROPERTIES_SUFFIX);
        }

        if (baseName != null) {
            String fullName = processBasename(baseName);
            resultBaseNameList.add(fullName);
        }
    }

    private String processBasename(String baseName) {
        String prefix = substringBeforeLast(baseName, "/");
        String name = substringAfterLast(baseName, "/");
        do {
            name = substringBeforeLast(name, "_");
        } while (name.contains("_"));
        String result = prefix + "/" + name;
        if (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}