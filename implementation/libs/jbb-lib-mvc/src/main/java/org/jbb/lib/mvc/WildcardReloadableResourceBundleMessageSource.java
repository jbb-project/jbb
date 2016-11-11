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

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.substringAfterLast;
import static org.apache.commons.lang3.StringUtils.substringBefore;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;
import static org.apache.commons.lang3.StringUtils.substringBetween;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

public class WildcardReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    @Override
    public void setBasenames(String... basenames) {
        if (basenames != null) {
            List<String> baseNames = new ArrayList<>();
            for (int i = 0; i < basenames.length; i++) {
                String basename = trimToEmpty(basenames[i]);
                if (isNotBlank(basename)) {
                    try {
                        Resource[] resources = resourcePatternResolver.getResources(basename);
                        for (int j = 0; j < resources.length; j++) {
                            Resource resource = resources[j];
                            String uri = resource.getURI().toString();
                            String baseName = null;
                            if (resource instanceof FileSystemResource) {
                                baseName = substringBetween(uri, "/classes/", ".properties");
                            } else if (resource instanceof ClassPathResource) {
                                baseName = substringBefore(uri, ".properties");
                            } else if (resource instanceof UrlResource) {
                                baseName = "classpath:" + substringBetween(uri, ".jar!/", ".properties");
                            }
                            if (baseName != null) {
                                String fullName = processBasename(baseName);
                                baseNames.add(fullName);
                            }
                        }
                    } catch (IOException e) {
                        logger.debug("No message source files found for basename " + basename + ".");
                    }
                }
                String[] resourceBasenames = baseNames.toArray(new String[]{});
                super.setBasenames(resourceBasenames);
            }
        }
    }

    String processBasename(String baseName) {
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