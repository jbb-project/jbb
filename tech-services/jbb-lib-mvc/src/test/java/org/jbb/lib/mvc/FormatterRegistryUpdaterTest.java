/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.collect.Maps;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import org.jbb.lib.commons.JbbBeanSearch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;

@RunWith(MockitoJUnitRunner.class)
public class FormatterRegistryUpdaterTest {

    @Mock
    private ApplicationContext appContextMock;

    private JbbBeanSearch jbbBeanSearch;
    private FormatterRegistryUpdater formatterRegistryUpdater;

    @Before
    public void setUp() throws Exception {
        jbbBeanSearch = new JbbBeanSearch(appContextMock);
    }

    @Test
    public void shouldAddAllFoundFormattersOnClasspath() throws Exception {
        // given
        FirstFormatter firstFormatter = mock(FirstFormatter.class);
        SecondFormatter secondFormatter = mock(SecondFormatter.class);

        Map<String, Formatter> formatterMap = Maps.newHashMap();
        formatterMap.put("firstFormatter", firstFormatter);
        formatterMap.put("secondFormatter", secondFormatter);

        given(appContextMock.getBeansOfType(eq(Formatter.class))).willReturn(formatterMap);

        FormatterRegistry formatterRegistryMock = mock(FormatterRegistry.class);

        // when
        formatterRegistryUpdater = new FormatterRegistryUpdater(jbbBeanSearch);
        formatterRegistryUpdater.fill(formatterRegistryMock);

        // then
        verify(formatterRegistryMock, times(1)).addFormatter(eq(firstFormatter));
        verify(formatterRegistryMock, times(1)).addFormatter(eq(secondFormatter));
    }

    private class FirstFormatter implements Formatter {

        @Override
        public Object parse(String text, Locale locale) throws ParseException {
            return null;
        }

        @Override
        public String print(Object object, Locale locale) {
            return null;
        }
    }

    private class SecondFormatter implements Formatter {

        @Override
        public Object parse(String text, Locale locale) throws ParseException {
            return null;
        }

        @Override
        public String print(Object object, Locale locale) {
            return null;
        }
    }
}