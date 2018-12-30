/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.oauth;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientSecretGeneratorTest {

    private ClientSecretGenerator clientSecretGenerator = new ClientSecretGenerator();

    @Test
    public void generatedSecretShouldHaveProperLength() {
        // when
        String generatedSecret = clientSecretGenerator.generateSecret();

        // then
        assertThat(generatedSecret.length()).isGreaterThanOrEqualTo(16);
        assertThat(generatedSecret.length()).isLessThanOrEqualTo(32);
    }
}