/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.commons.dependencies;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
class DependencyResolver {

    private static final String DEPENDENCIES_FILENAME = "dependencies.data";

    private static final Pattern DEPENDENCY_LINE_PATTERN = Pattern.compile("^([^:]+):([^:]+):([^:]+):([^:]+)$");

    @PostConstruct
    public void logDependencies() {
        log.debug("jBB runs with the following 3th party libraries: \n{}", formatDependencies());
    }

    private String formatDependencies() {
        return getAllDependencies().stream()
                .map(Dependency::toString)
                .collect(Collectors.joining("\n"));
    }

    public List<Dependency> getAllDependencies() {
        return getDependencyFileLines().map(this::mapToModel)
                .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                .collect(Collectors.toList());
    }

    private Optional<Dependency> mapToModel(String line) {
        String trimmedLine = line.trim();
        Matcher m = DEPENDENCY_LINE_PATTERN.matcher(trimmedLine);
        if (m.matches()) {
            return Optional.of(Dependency.builder()
                    .groupId(m.group(1))
                    .artifactId(m.group(2))
                    .versionId(m.group(4))
                    .build());
        }
        return Optional.empty();
    }

    private Stream<String> getDependencyFileLines() {
        try {
            return Files.lines(Paths.get(new ClassPathResource(DEPENDENCIES_FILENAME).getURI()));
        } catch (IOException e) {
            log.warn("Missing dependency.data file in classpath", e);
            return Stream.empty();
        }
    }
}
