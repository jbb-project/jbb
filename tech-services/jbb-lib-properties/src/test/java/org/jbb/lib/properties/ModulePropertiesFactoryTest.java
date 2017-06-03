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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ModulePropertiesFactoryTest {
    @Mock
    private FreshInstallPropertiesCreator propertiesCreatorMock;

    @Mock
    private UpdateFilePropertyChangeListenerFactoryBean propChangeFactoryMock;

    @Mock
    private LoggingPropertyChangeListener logPropListenerMock;

    @InjectMocks
    private ModulePropertiesFactory modulePropertiesFactory;

    @Test
    public void shouldNotAddListeners_whenPropertiesAreStatic() throws Exception {
        // when
        modulePropertiesFactory.create(TestbedModuleStaticProperties.class);

        // then
        verifyZeroInteractions(propChangeFactoryMock, logPropListenerMock);
    }

    @Test
    public void shouldAddListeners_whenPropertiesAreDynamic() throws Exception {
        // given
        given(propChangeFactoryMock.setClass(eq(TestbedModuleProperties.class))).willReturn(propChangeFactoryMock);

        // when
        modulePropertiesFactory.create(TestbedModuleProperties.class);

        // then
        verify(propChangeFactoryMock, times(1)).setClass(eq(TestbedModuleProperties.class));
    }

    private interface TestbedModuleStaticProperties extends ModuleStaticProperties {

    }

    private interface TestbedModuleProperties extends ModuleProperties {

    }
}