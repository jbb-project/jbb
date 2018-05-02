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
import org.jbb.system.web.metrics.logic.MetricsSettingsFormTranslator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/acp/system/metrics")
public class AcpMetricSettingsController {
    private static final String VIEW_NAME = "acp/system/metrics";
    private static final String METRIC_SETTINGS_FORM = "metricSettingsForm";
    private static final String FORM_SAVED_FLAG = "metricSettingsFormSaved";

    private final MetricSettingsService metricSettingsService;
    private final MetricsSettingsFormTranslator formTranslator;

    @RequestMapping(method = RequestMethod.GET)
    public String systemMetricSettingsGet(Model model) {
        MetricSettings metricSettings = metricSettingsService.getMetricSettings();
        model.addAttribute(METRIC_SETTINGS_FORM, formTranslator.toForm(metricSettings));
        return VIEW_NAME;
    }
}
