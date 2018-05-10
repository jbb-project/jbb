/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties.health;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.properties.JbbPropertyFilesResolver;
import org.jbb.lib.properties.ModuleStaticProperties;
import org.jbb.lib.properties.PropertiesUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonitoredPropertyFilesProvider {

    private final JbbPropertyFilesResolver jbbPropertyFilesResolver;
    private final ApplicationContext applicationContext;

    public Map<ModuleStaticProperties, File> getMonitoredFiles() {
        return new ArrayList<>(
            applicationContext.getBeansOfType(ModuleStaticProperties.class)
                .values()).stream().collect(Collectors.toMap(Function.identity(), this::getFile))
            .entrySet().stream()
            .filter(e -> e.getValue().isPresent())
            .collect(Collectors.toMap(Entry::getKey, e -> e.getValue().get()));
    }

    private Optional<File> getFile(ModuleStaticProperties moduleProperties) {
        List<File> files = jbbPropertyFilesResolver.resolvePropertyFileNames(
            PropertiesUtils.getUnproxyClass(moduleProperties)).stream()
            .map(File::new).limit(1).collect(Collectors.toList());
        return files.isEmpty() ? Optional.empty() : Optional.of(files.get(0));
    }

}
