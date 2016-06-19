/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties;

import com.google.common.collect.Sets;

import org.aeonbits.owner.Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FreshInstallPropertiesCreatorTest {
    @Mock
    private JbbPropertyFilesResolver propertyFilesResolverMock;

    @InjectMocks
    private FreshInstallPropertiesCreator propertiesCreator;

    @Before
    public void setUp() throws Exception {
        assertThat(propertyFilesResolverMock).isNotNull();
        assertThat(propertiesCreator).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenNullPassed() throws Exception {
        // given
        Class<? extends ModuleProperties> nullClass = null;

        // when
        propertiesCreator.putDefaultPropertiesIfNeeded(nullClass);

        // then
        // throw IllegalArgumentException
    }

    @Test
    public void shouldDoNothing_whenEmptySetPassed() throws Exception {
        // given
        when(propertyFilesResolverMock.resolvePropertyFileNames(any(Class.class))).thenReturn(Sets.newHashSet());

        // when
        propertiesCreator.putDefaultPropertiesIfNeeded(ClasspathProperties.class);

        // then
        // nothing...
    }

    @Config.Sources({"classpath:test.properties"})
    private interface ClasspathProperties extends ModuleProperties {

        String foo();

        String bar();
    }
}