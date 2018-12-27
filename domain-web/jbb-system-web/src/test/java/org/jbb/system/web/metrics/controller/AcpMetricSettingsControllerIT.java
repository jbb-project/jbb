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

import com.google.common.collect.Sets;

import org.jbb.system.api.metrics.MetricSettings;
import org.jbb.system.api.metrics.MetricSettingsService;
import org.jbb.system.api.metrics.MetricType;
import org.jbb.system.api.metrics.MetricsConfigException;
import org.jbb.system.api.metrics.reporter.ConsoleMetricReporterSettings;
import org.jbb.system.api.metrics.reporter.CsvMetricReporterSettings;
import org.jbb.system.api.metrics.reporter.JmxMetricReporterSettings;
import org.jbb.system.api.metrics.reporter.LogMetricReporterSettings;
import org.jbb.system.web.BaseIT;
import org.jbb.system.web.metrics.form.MetricsSettingsForm;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class AcpMetricSettingsControllerIT extends BaseIT {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    private MetricSettingsService metricSettingsServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
        Mockito.reset(metricSettingsServiceMock);
    }

    @Test
    public void shouldPutCurrentMetricSettingsToModel_whenGET() throws Exception {
        // given
        givenCurrentMetricSettings();

        // when
        ResultActions result = mockMvc.perform(get("/acp/system/metrics"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/system/metrics"));

        MetricsSettingsForm metricSettingsForm = (MetricsSettingsForm) result.andReturn()
                .getModelAndView().getModel().get("metricSettingsForm");

        assertThat(metricSettingsForm).isNotNull();
    }

    @Test
    public void shouldSetFlag_whenPOST_ok() throws Exception {
        // given
        givenCurrentMetricSettings();

        // when
        ResultActions result = mockMvc.perform(post("/acp/system/metrics")
                .param("consoleReporterSettings.enabled", "true")
                .param("consoleReporterSettings.periodSeconds", "60")
                .param("consoleReporterSettings.metricTypes[JVM]", "true")
                .param("jmxReporterSettings.enabled", "true")
                .param("jmxReporterSettings.metricTypes[JVM]", "true")
                .param("csvReporterSettings.enabled", "true")
                .param("csvReporterSettings.periodSeconds", "60")
                .param("csvReporterSettings.metricTypes[JVM]", "true")
                .param("logReporterSettings.enabled", "true")
                .param("logReporterSettings.periodSeconds", "60")
                .param("logReporterSettings.metricTypes[JVM]", "true")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/system/metrics"))
                .andExpect(flash().attribute("metricSettingsFormSaved", true));

        verify(metricSettingsServiceMock, times(1)).setMetricSettings(any(MetricSettings.class));
    }

    @Test
    public void shouldNotSetFlag_whenPOST_andBindingErrorExist() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/acp/system/metrics")
                .param("consoleReporterSettings.enabled", "xxx")
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/system/metrics"))
                .andExpect(model().attribute("metricSettingsFormSaved", false));
    }

    @Test
    public void shouldNotSetFlag_whenPOST_andServiceThrowException() throws Exception {
        // given
        MetricsConfigException exceptionMock = mock(MetricsConfigException.class);
        ConstraintViolation violationMock = mock(ConstraintViolation.class);
        Path pathMock = mock(Path.class);
        given(pathMock.toString()).willReturn("consoleReporterSettings.periodSeconds");
        given(violationMock.getPropertyPath()).willReturn(pathMock);
        given(violationMock.getMessage()).willReturn("violation");
        given(exceptionMock.getConstraintViolations()).willReturn(Sets.newHashSet(violationMock));

        willThrow(exceptionMock)
                .given(metricSettingsServiceMock)
                .setMetricSettings(any(MetricSettings.class));

        // when
        ResultActions result = mockMvc.perform(post("/acp/system/metrics")
                .param("consoleReporterSettings.enabled", "true")
                .param("consoleReporterSettings.periodSeconds", "-60")
                .param("consoleReporterSettings.metricTypes[JVM]", "true")
                .param("jmxReporterSettings.enabled", "true")
                .param("jmxReporterSettings.metricTypes[JVM]", "true")
                .param("csvReporterSettings.enabled", "true")
                .param("csvReporterSettings.periodSeconds", "60")
                .param("csvReporterSettings.metricTypes[JVM]", "true")
                .param("logReporterSettings.enabled", "true")
                .param("logReporterSettings.periodSeconds", "60")
                .param("logReporterSettings.metricTypes[JVM]", "true")
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/system/metrics"))
                .andExpect(model().attribute("metricSettingsFormSaved", false));
    }

    private void givenCurrentMetricSettings() {
        MetricSettings metricSettings = MetricSettings.builder()
                .consoleReporterSettings(ConsoleMetricReporterSettings.builder()
                        .enabled(true)
                        .periodSeconds(60)
                        .supportedTypes(Sets.newHashSet(MetricType.values()))
                        .build())
                .csvReporterSettings(CsvMetricReporterSettings.builder()
                        .enabled(true)
                        .periodSeconds(60)
                        .supportedTypes(Sets.newHashSet(MetricType.values()))
                        .build())
                .jmxReporterSettings(JmxMetricReporterSettings.builder()
                        .enabled(false)
                        .supportedTypes(Sets.newHashSet(MetricType.values()))
                        .build())
                .logReporterSettings(LogMetricReporterSettings.builder()
                        .enabled(true)
                        .periodSeconds(60)
                        .supportedTypes(Sets.newHashSet(MetricType.values()))
                        .build())
                .build();

        given(metricSettingsServiceMock.getMetricSettings()).willReturn(metricSettings);
    }

}