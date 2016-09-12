/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc;

import com.google.common.collect.Sets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;

import java.text.ParseException;
import java.util.Locale;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FormatterRegistryUpdaterTest {
    @Mock
    private Reflections reflectionsMock;

    @Mock
    private ApplicationContext appContextMock;

    private FormatterRegistryUpdater formatterRegistryUpdater;

    @Test
    public void shouldAddAllFoundFormattersOnClasspath() throws Exception {
        // given
        FirstFormatter firstFormatter = mock(FirstFormatter.class);
        SecondFormatter secondFormatter = mock(SecondFormatter.class);
        given(reflectionsMock.getSubTypesOf(eq(Formatter.class)))
                .willAnswer(invocationOnMock -> Sets.newHashSet(FirstFormatter.class, SecondFormatter.class));

        formatterRegistryUpdater = new FormatterRegistryUpdater(reflectionsMock);
        formatterRegistryUpdater.setApplicationContext(appContextMock);

        given(appContextMock.getBean(eq(FirstFormatter.class))).willReturn(firstFormatter);
        given(appContextMock.getBean(eq(SecondFormatter.class))).willReturn(secondFormatter);

        FormatterRegistry formatterRegistryMock = mock(FormatterRegistry.class);

        // when
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