/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.metrics.controller;

import org.jbb.system.api.metrics.MetricSettings;
import org.jbb.system.api.metrics.MetricSettingsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/acp/system/metrics")
@Slf4j
@RequiredArgsConstructor
public class AcpMetricSettingsController {
    private static final String VIEW_NAME = "acp/system/metrics";
    private static final String METRIC_SETTINGS_FORM = "metricSettingsForm";
    private static final String FORM_SAVED_FLAG = "metricSettingsFormSaved";

    private final MetricSettingsService metricSettingsService;

    @RequestMapping(method = RequestMethod.GET)
    public String systemMetricSettingsGet(Model model) {
        MetricSettings metricSettings = metricSettingsService.getMetricSettings();
        return VIEW_NAME;
    }
}
