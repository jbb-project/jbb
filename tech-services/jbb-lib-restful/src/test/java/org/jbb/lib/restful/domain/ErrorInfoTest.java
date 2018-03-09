/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.restful.domain;

import org.assertj.core.util.Lists;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ErrorInfoTest {

    @Test
    public void shouldJoinManyErrors_intoOneString() {
        // when
        String result = ErrorInfo.joinedMessages(Lists.newArrayList(
                ErrorInfo.MEMBER_NOT_FOUND,
                ErrorInfo.GET_NOT_OWN_ACCOUNT
        ));

        // then
        assertThat(result).isEqualTo("JBB-101: Member not found,\n" +
                "JBB-105: Cannot get not own account");
    }
}