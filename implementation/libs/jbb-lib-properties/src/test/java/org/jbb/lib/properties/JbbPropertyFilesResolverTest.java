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

import org.aeonbits.owner.Config.Sources;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JbbHomePath.class)
public class JbbPropertyFilesResolverTest {
    private JbbPropertyFilesResolver propertyFilesResolver;

    @Before
    public void setUp() throws Exception {
        propertyFilesResolver = new JbbPropertyFilesResolver();

        assertThat(propertyFilesResolver).isNotNull();

        PowerMockito.mockStatic(JbbHomePath.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenNullPassed() throws Exception {
        // given
        Class<? extends ModuleProperties> nullClass = null;

        // when
        propertyFilesResolver.resolvePropertyFileNames(nullClass);

        // then
        // throw IllegalArgumentException
    }


    @Test
    public void shouldReplaceJbbHomePlaceholder_whenPropertiesFromJbbHomePathPassed() throws Exception {
        // given
        Class propertiesClass = TestProperties.class;

        when(JbbHomePath.getEffective()).thenReturn("/opt");

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