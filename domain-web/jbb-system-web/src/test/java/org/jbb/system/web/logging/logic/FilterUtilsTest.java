/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.logging.logic;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.jbb.system.api.model.logging.LogFilter;
import org.jbb.system.api.model.logging.LogLevel;
import org.jbb.system.api.model.logging.LogLevelFilter;
import org.jbb.system.api.model.logging.LogThresholdFilter;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FilterUtilsTest {

    @Test
    public void shouldReturnNoneAsFilterText_whenNullPassed() throws Exception {
        // when
        String filterText = FilterUtils.getFilterText(null);

        // then
        assertThat(filterText).isEqualTo("None");
    }

    @Test
    public void shouldReturnFilterText_forLogLevelFilter() throws Exception {
        // when
        String filterText = FilterUtils.getFilterText(new LogLevelFilter(LogLevel.TRACE));

        // then
        assertThat(filterText).isEqualTo("Level: trace");
    }

    @Test
    public void shouldReturnFilterText_forLogThresholdFilter() throws Exception {
        // when
        String filterText = FilterUtils.getFilterText(new LogThresholdFilter(LogLevel.OFF));

        // then
        assertThat(filterText).isEqualTo("Threshold: off");
    }

    @Test
    public void shouldReturnDefaultFilterText_forUnknownImplementation() throws Exception {
        // given
        LogFilter mockFilter = Mockito.mock(LogFilter.class);
        Mockito.when(mockFilter.toString()).thenReturn("TESTBED");

        //when
        String filterText = FilterUtils.getFilterText(mockFilter);

        // then
        assertThat(filterText).isEqualTo(mockFilter.toString());
    }

    @Test
    public void shouldReturnNullFilter_fromNullOrEmptyOrNoneString() throws Exception {

        // when then
        assertThat(FilterUtils.getFilterFromString(null)).isNull();
        assertThat(FilterUtils.getFilterFromString(StringUtils.EMPTY)).isNull();
        assertThat(FilterUtils.getFilterFromString("None")).isNull();
        assertThat(FilterUtils.getFilterFromString("NONE")).isNull();
    }

    @Test
    public void shouldReturnLogLevelFilter_whenStringStartsWithLevel() throws Exception {
        // when
        LogFilter logFilter = FilterUtils.getFilterFromString("Level: info");

        // then
        assertThat(logFilter).isInstanceOf(LogLevelFilter.class);
        assertThat(((LogLevelFilter) logFilter).getLogLevel()).isEqualTo(LogLevel.INFO);
    }

    @Test
    public void shouldReturnLogThresholdFilter_whenStringStartsWithThreshold() throws Exception {
        // when
        LogFilter logFilter = FilterUtils.getFilterFromString("Threshold: warn");

        // then
        assertThat(logFilter).isInstanceOf(LogThresholdFilter.class);
        assertThat(((LogThresholdFilter) logFilter).getLogLevel()).isEqualTo(LogLevel.WARN);
    }

    @Test
    public void shouldReturnNullFilter_whenStringIncorrect() throws Exception {
        // when then
        assertThat(FilterUtils.getFilterFromString("blebleble")).isNull();
    }

    @Test
    public void filterListTest() throws Exception {
        // when
        List<String> filterList = FilterUtils.getAllFiltersList();

        // then
        assertThat(filterList).isEqualTo(Lists.newArrayList("None",
                "Level: all", "Level: trace", "Level: debug", "Level: info", "Level: warn", "Level: error", "Level: off",
                "Threshold: all", "Threshold: trace", "Threshold: debug", "Threshold: info", "Threshold: warn", "Threshold: error", "Threshold: off"
        ));

    }
}