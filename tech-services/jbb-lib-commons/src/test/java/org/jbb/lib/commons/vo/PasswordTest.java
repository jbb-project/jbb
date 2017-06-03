/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.commons.vo;

import org.jbb.lib.commons.vo.Password;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordTest {

    @Test
    public void pojoTest() throws Exception {
        // when
        Password password = Password.builder().value("pass".toCharArray()).build();
        Password passwordConstr = new Password("pass".toCharArray());


        // then
        assertThat(password.getValue()).isEqualTo("pass".toCharArray());
        assertThat(passwordConstr.getValue()).isEqualTo("pass".toCharArray());

        assertThat(password.hashCode()).isEqualTo(passwordConstr.hashCode());

        assertThat(password).isEqualTo(password);
        assertThat(password).isNotEqualTo(null);

        assertThat(password).isEqualTo(passwordConstr);
    }

}