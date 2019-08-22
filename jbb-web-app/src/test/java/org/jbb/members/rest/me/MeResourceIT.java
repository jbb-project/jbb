/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.me;

import org.jbb.BaseIT;
import org.junit.Test;

import io.restassured.module.mockmvc.response.MockMvcResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jbb.ApiRequestUtils.basicAuthAdminApiRequest;
import static org.jbb.ApiRequestUtils.noAuthApiRequest;
import static org.jbb.ApiRequestUtils.responseBodyAs;

public class MeResourceIT extends BaseIT {

    @Test
    public void meResponseForAnonymous() {
        // when
        MockMvcResponse response = noAuthApiRequest().get("/api/v1/me");

        // then
        MeDataDto meData = responseBodyAs(response, MeDataDto.class);
        assertThat(meData.getCurrentMember()).isNull();
        assertThat(meData.getCurrentOAuthClient()).isNull();
        assertThat(meData.getSessionId()).isNull();
    }

    @Test
    public void meResponseForAdmin() {
        // when
        MockMvcResponse response = basicAuthAdminApiRequest().get("/api/v1/me");

        // then
        MeDataDto meData = responseBodyAs(response, MeDataDto.class);
        assertThat(meData.getCurrentMember().getAdministrator()).isTrue();
        assertThat(meData.getCurrentOAuthClient()).isNull();
    }

}
