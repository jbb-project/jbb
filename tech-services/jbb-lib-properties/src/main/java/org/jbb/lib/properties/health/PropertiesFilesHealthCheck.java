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
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.health.JbbHealthCheck;
import org.jbb.lib.properties.JbbPropertyFilesResolver;
import org.jbb.lib.properties.ModuleStaticProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PropertiesFilesHealthCheck extends JbbHealthCheck {

    private final JbbPropertyFilesResolver jbbPropertyFilesResolver;
    private final ApplicationContext applicationContext;

    private Map<ModuleStaticProperties, Optional<File>> filesMonitored;

    @PostConstruct
    public void fillMonitoredFiles() {
        filesMonitored = new ArrayList<>(
            applicationContext.getBeansOfType(ModuleStaticProperties.class).values()).stream()
            .collect(Collectors.toMap(Function.identity(),
                this::getFile));
    }

    private Optional<File> getFile(ModuleStaticProperties moduleProperties) {
        List<File> files = jbbPropertyFilesResolver.resolvePropertyFileNames(
            (Class<? extends ModuleStaticProperties>) moduleProperties.getClass()
                .getInterfaces()[0])
            .stream()
            .map(File::new).limit(1).collect(Collectors.toList());
        return files.isEmpty() ? Optional.empty() : Optional.of(files.get(0));
    }

    @Override
    public String getName() {
        return "Properties files";
    }

    @Override
    protected Result check() throws Exception {
        ResultBuilder builder = Result.builder();
        boolean healthyFlag = true;
        for (Entry<ModuleStaticProperties, Optional<File>> moduleProperties : filesMonitored
            .entrySet()) {
            String modulePropertiesName = moduleProperties.getKey().getClass().getInterfaces()[0]
                .getSimpleName();
            Optional<File> propertyFileOptional = moduleProperties.getValue();

            String message = null;
            if (propertyFileOptional.isPresent()) {
                File propertyFile = propertyFileOptional.get();
                if (!propertyFile.exists()) {
                    message = "'File %s not found'";
                } else if (propertyFile.isDirectory()) {
                    message = "'File %s is a directory'";
                } else if (!propertyFile.canRead()) {
                    message = "'File %s is not readable'";
                } else if (!propertyFile.canWrite()) {
                    message = "'File %s is not writable'";
                }
            }

            if (message != null) {
                healthyFlag = false;
            } else {
                message = "OK";
            }
            builder.withDetail(modulePropertiesName,
                String.format(message, propertyFileOptional.map(File::getName).orElse("")));
        }
        return healthyFlag ? builder.healthy().build() :
            builder.withMessage("Some property files are corrupted").unhealthy().build();
    }
}
