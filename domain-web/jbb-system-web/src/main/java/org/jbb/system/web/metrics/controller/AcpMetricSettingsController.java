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

import org.jbb.lib.mvc.SimpleErrorsBindingMapper;
import org.jbb.system.api.metrics.MetricSettings;
import org.jbb.system.api.metrics.MetricSettingsService;
import org.jbb.system.api.metrics.MetricsConfigException;
import org.jbb.system.web.metrics.form.MetricsSettingsForm;
import org.jbb.system.web.metrics.logic.MetricsSettingsFormTranslator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/acp/system/metrics")
public class AcpMetricSettingsController {
    private static final String VIEW_NAME = "acp/system/metrics";
    private static final String METRIC_SETTINGS_FORM = "metricSettingsForm";
    private static final String FORM_SAVED_FLAG = "metricSettingsFormSaved";

    private final MetricSettingsService metricSettingsService;
    private final MetricsSettingsFormTranslator formTranslator;
    private final SimpleErrorsBindingMapper errorsBindingMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String systemMetricSettingsGet(Model model) {
        MetricSettings metricSettings = metricSettingsService.getMetricSettings();
        model.addAttribute(METRIC_SETTINGS_FORM, formTranslator.toForm(metricSettings));
        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String systemMetricSettingsPost(Model model,
        @ModelAttribute(METRIC_SETTINGS_FORM) MetricsSettingsForm form,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.debug("Metrics settings form error detected: {}", bindingResult.getAllErrors());
            model.addAttribute(FORM_SAVED_FLAG, false);
            return VIEW_NAME;
        }

        try {
            metricSettingsService.setMetricSettings(formTranslator.toModel(form));
        } catch (MetricsConfigException e) {
            errorsBindingMapper.map(e.getConstraintViolations(), bindingResult);
            model.addAttribute(FORM_SAVED_FLAG, false);
            return VIEW_NAME;
        }

        redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);
        return "redirect:/" + VIEW_NAME;
    }
}
