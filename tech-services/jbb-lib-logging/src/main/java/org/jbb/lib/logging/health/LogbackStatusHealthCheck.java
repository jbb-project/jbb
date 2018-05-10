/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.logging.health;

import ch.qos.logback.core.status.Status;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.health.JbbHealthCheck;
import org.jbb.lib.logging.LoggingBootstrapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogbackStatusHealthCheck extends JbbHealthCheck {

    public static final String REGISTERING_FALLBACK_MESSAGE = "after registerSafeConfiguration";

    private final LoggingBootstrapper loggingBootstrapper;

    @Override
    public String getName() {
        return "Logging framework";
    }

    @Override
    protected Result check() throws Exception {
        // verify logback config file
        File logConfigFile = new File(loggingBootstrapper.getLogConfFilePath());
        if (!logConfigFile.exists()) {
            return Result.unhealthy("Logback config file is not found");
        } else if (logConfigFile.isDirectory()) {
            return Result.unhealthy("Logback config file is a directory");
        } else if (!logConfigFile.canRead()) {
            return Result.unhealthy("Logback config file is not readable");
        } else if (!logConfigFile.canWrite()) {
            return Result.unhealthy("Logback config file is not writable");
        }

        // verify logback framework last statuses
        List<Status> invalidStatuses = LogbackStateStorage.getLastStatuses().stream()
            .filter(
                status -> status.getLevel() == Status.ERROR || fallbackWasBeingRegistered(status))
            .collect(Collectors.toList());

        if (invalidStatuses.isEmpty()) {
            return Result.healthy();
        } else {
            return Result.builder()
                .withMessage("Logback is not working properly")
                .withDetail("invalidStatuses", invalidStatuses)
                .unhealthy()
                .build();
        }
    }

    private boolean fallbackWasBeingRegistered(Status status) {
        return StringUtils.contains(status.getMessage(), REGISTERING_FALLBACK_MESSAGE);
    }
}
