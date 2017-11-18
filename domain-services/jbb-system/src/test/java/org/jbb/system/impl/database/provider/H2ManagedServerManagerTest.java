/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.provider;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.lib.db.DbProperties;
import org.jbb.system.api.database.DatabaseProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class H2ManagedServerManagerTest {

    @Mock
    DbProperties dbPropertiesMock;

    @InjectMocks
    H2ManagedServerManager h2ManagedServerManager;

    @Test
    public void shouldReturnCorrectProvider() throws Exception {
        // when
        DatabaseProvider providerName = h2ManagedServerManager.getProviderName();

        // then
        assertThat(providerName).isEqualTo(DatabaseProvider.H2_MANAGED_SERVER);
    }

}