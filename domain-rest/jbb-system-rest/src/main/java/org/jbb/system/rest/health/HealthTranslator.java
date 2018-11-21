/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.health;

import lombok.RequiredArgsConstructor;
import org.jbb.lib.commons.JbbMetaData;
import org.jbb.system.api.health.HealthResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HealthTranslator {

    private final JbbMetaData jbbMetaData;

    public HealthDto toDto(HealthResult healthResult) {
        return HealthDto.builder()
            .applicationVersion(jbbMetaData.jbbVersion())
            .status(healthResult.getStatus())
            .lastCheckedAt(healthResult.getLastCheckedAt())
            .build();
    }

}
