/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.health.checks;

import java.io.File;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.commons.JbbMetaData;
import org.jbb.lib.health.JbbHealthCheck;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JbbDirectoryHealthCheck extends JbbHealthCheck {

    private final JbbMetaData jbbMetaData;

    @Override
    public String getName() {
        return "jBB Directory";
    }

    @Override
    protected Result check() throws Exception {
        File jbbDirectory = new File(jbbMetaData.jbbHomePath());
        if (!jbbDirectory.exists()) {
            return Result.unhealthy("jBB directory does not exist");
        } else if (!jbbDirectory.isDirectory()) {
            return Result.unhealthy("jBB directory is a file");
        }
        return Result.healthy();
    }
}
