/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties;

import org.aeonbits.owner.Config.Sources;
import org.jbb.lib.commons.JbbMetaData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JbbPropertyFilesResolverTest {
    @Mock
    private JbbMetaData jbbMetaDataMock;

    @InjectMocks
    private JbbPropertyFilesResolver propertyFilesResolver;

    @Before
    public void setUp() throws Exception {
        assertThat(propertyFilesResolver).isNotNull();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullPassed() throws Exception {
        // given
        Class<? extends ModuleProperties> nullClass = null;

        // when
        propertyFilesResolver.resolvePropertyFileNames(nullClass);

        // then
        // throw NullPointerException
    }


    @Test
    public void shouldReplaceJbbHomePlaceholder_whenPropertiesFromJbbHomePathPassed() throws Exception {
        // given
        Class propertiesClass = TestProperties.class;

        when(jbbMetaDataMock.jbbHomePath()).thenReturn("/opt");

        // when
        Set<String> modulePropertyFiles = propertyFilesResolver.resolvePropertyFileNames(propertiesClass);

        // then
        assertThat(modulePropertyFiles).hasSize(2);
        assertThat(modulePropertyFiles).contains("/opt/test1.properties");
        assertThat(modulePropertyFiles).contains("/opt/test2.properties");
    }

    @Test
    public void shouldReturnEmptySet_whenPropertiesNotFromJbbHomePathPassed() throws Exception {
        // given
        Class propertiesClass = OnlyClasspathProperties.class;

        // when
        Set<String> modulePropertyFiles = propertyFilesResolver.resolvePropertyFileNames(propertiesClass);

        // then
        assertThat(modulePropertyFiles).isEmpty();
    }

    @Sources({"file:${jbb.home}/test1.properties", "file:${jbb.home}/test2.properties", "classpath:test.properties"})
    private interface TestProperties extends ModuleProperties {

        String foo();

        String bar();
    }

    @Sources({"classpath:test.properties", "classpath:test2.properties", "classpath:test3.properties"})
    private interface OnlyClasspathProperties extends ModuleProperties {

        String foo();

        String bar();
    }

}