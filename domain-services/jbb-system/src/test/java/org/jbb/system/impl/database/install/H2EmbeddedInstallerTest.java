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


import org.jbb.system.api.database.DatabaseProvider;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class H2EmbeddedInstallerTest {

    private H2EmbeddedInstaller h2EmbeddedInstaller;

    @Before
    public void setUp() {
        h2EmbeddedInstaller = new H2EmbeddedInstaller();
    }

    @Test
    public void isApplicableTest() {
        // when
        boolean result = h2EmbeddedInstaller.isApplicable(DatabaseProvider.H2_EMBEDDED);

        // then
        assertThat(result).isTrue();
    }
}