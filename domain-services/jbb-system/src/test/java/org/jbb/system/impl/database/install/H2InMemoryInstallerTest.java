/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.install;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.system.api.database.DatabaseProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class H2InMemoryInstallerTest {

    @InjectMocks
    private H2InMemoryInstaller h2InMemoryInstaller;

    @Test
    public void h2InMemoryProviderShouldBeApplicable() {
        // when
        boolean applicable = h2InMemoryInstaller.isApplicable(DatabaseProvider.H2_IN_MEMORY);

        // then
        assertThat(applicable).isTrue();
    }


}