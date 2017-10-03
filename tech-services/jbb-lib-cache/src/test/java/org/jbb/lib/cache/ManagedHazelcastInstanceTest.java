/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.cache;

import static org.mockito.Mockito.verify;

import com.hazelcast.core.HazelcastInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ManagedHazelcastInstanceTest {

    @Mock
    private HazelcastInstance targetInstanceMock;

    @InjectMocks
    private ManagedHazelcastInstance managedHazelcastInstance;

    @Test
    public void shutdownInstanceIfIsPresent() throws Exception {
        // when
        managedHazelcastInstance.shutdownIfApplicable();

        // then
        verify(targetInstanceMock).shutdown();
    }
}