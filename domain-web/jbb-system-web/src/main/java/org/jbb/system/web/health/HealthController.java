/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.health;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jbb.system.api.health.HealthCheckService;
import org.jbb.system.api.health.HealthResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {

    private static final String VIEW_NAME = "health";
    private static final String HEALTH_STATUS = "healthStatus";
    private static final String HEALTH_LAST_CHECK = "healthLastCheck";

    private final HealthCheckService healthCheckService;

    @RequestMapping(method = RequestMethod.GET)
    public String healthGet(Model model) {
        HealthResult result = healthCheckService.getHealth();
        model.addAttribute(HEALTH_STATUS, result.getStatus().toString());
        model.addAttribute(HEALTH_LAST_CHECK, result.getLastCheckedAt().toString());
        return VIEW_NAME;
    }

}
