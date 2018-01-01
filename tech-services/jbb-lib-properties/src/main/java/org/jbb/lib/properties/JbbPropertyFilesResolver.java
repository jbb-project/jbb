/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties;

import com.google.common.collect.Sets;

import org.aeonbits.owner.Config;
import org.apache.commons.lang3.Validate;
import org.jbb.lib.commons.JbbMetaData;
import org.springframework.stereotype.Component;

import java.util.Set;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class JbbPropertyFilesResolver {
    private static final String JBB_HOME_PREFIX = "file:${jbb.home}";

    private final JbbMetaData metaData;

    private static boolean fileIsInJbbHome(String sourceRawString) {
        return sourceRawString.startsWith(JBB_HOME_PREFIX);
    }

    public Set<String> resolvePropertyFileNames(Class<? extends ModuleStaticProperties> clazz) {
        Validate.notNull(clazz, "Class cannot be null");
        Set<String> result = Sets.newHashSet();
        Config.Sources annotation = clazz.getAnnotation(Config.Sources.class);
        for (String sourceRawString : annotation.value()) {
            if (fileIsInJbbHome(sourceRawString)) {
                String resolvedFilePath = resolveFullPath(sourceRawString);
                result.add(resolvedFilePath);
            }
        }
        return result;
    }

    private String resolveFullPath(String sourceRawString) {
        return sourceRawString.replace(JBB_HOME_PREFIX, metaData.jbbHomePath());
    }
}
