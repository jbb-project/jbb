/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.jbb.lib.test.PojoTest;
import org.junit.Test;

public class PasswordEntityTest extends PojoTest {

    @Override
    public Class getClassUnderTest() {
        return PasswordEntity.class;
    }

    @Test
    public void builderTest() throws Exception {
        // when
        PasswordEntity passwordEntity = PasswordEntity.builder()
                .memberId(3993L)
                .password("foivmeiomc")
                .applicableSince(LocalDateTime.now())
                .build();

        // then
        assertThat(passwordEntity).isNotNull();
    }

}