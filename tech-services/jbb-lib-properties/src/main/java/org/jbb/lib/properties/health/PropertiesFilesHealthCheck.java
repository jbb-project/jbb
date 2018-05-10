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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.health.JbbHealthCheck;
import org.jbb.lib.properties.ModuleStaticProperties;
import org.jbb.lib.properties.PropertiesUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PropertiesFilesHealthCheck extends JbbHealthCheck {

    private final MonitoredPropertyFilesProvider monitoredPropertyFilesProvider;

    private Map<ModuleStaticProperties, File> filesMonitored;

    @PostConstruct
    public void fillMonitoredFiles() {
        filesMonitored = monitoredPropertyFilesProvider.getMonitoredFiles();
    }

    @Override
    public String getName() {
        return "Properties files";
    }

    @Override
    protected Result check() throws Exception {

        List<ModulePropertiesResult> partialResults = filesMonitored.entrySet().stream()
            .map(entry -> checkModule(entry.getKey(), entry.getValue()))
            .sorted(Comparator.comparing(ModulePropertiesResult::getModuleName))
            .collect(Collectors.toList());

        ResultBuilder builder = Result.builder();
        partialResults
            .forEach(result -> builder.withDetail(result.getModuleName(), result.getMessage()));

        return partialResults.stream().allMatch(ModulePropertiesResult::isHealthy) ?
            builder.healthy().build() :
            builder.withMessage("Some property file(s) is/are in invalid state").unhealthy()
                .build();
    }

    private ModulePropertiesResult checkModule(ModuleStaticProperties properties,
        File propertyFile) {
        String errorMessage = null;

        if (!propertyFile.exists()) {
            errorMessage = "'File %s not found'";
        } else if (propertyFile.isDirectory()) {
            errorMessage = "'File %s is a directory'";
        } else if (!propertyFile.canRead()) {
            errorMessage = "'File %s is not readable'";
        } else if (!propertyFile.canWrite()) {
            errorMessage = "'File %s is not writable'";
        }

        return ModulePropertiesResult.builder()
            .moduleName(PropertiesUtils.getUnproxyClass(properties).getSimpleName())
            .healthy(errorMessage == null)
            .message(
                errorMessage == null ? "OK" : String.format(errorMessage, propertyFile.getName()))
            .build();
    }

    @Data
    @Builder
    @AllArgsConstructor
    private static class ModulePropertiesResult {

        private String moduleName;
        private boolean healthy;
        private String message;
    }

}
