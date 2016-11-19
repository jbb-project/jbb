/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.base.controller;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AcpMonitoringRedirectControllerTest {
    @Test
    public void shouldRedirectToJavaMelodyMonitoring() throws Exception {
        // given
        AcpMonitoringRedirectController controller = new AcpMonitoringRedirectController();

        // when
        String resultView = controller.redirectToMonitoringGet();

        // then
        assertThat(resultView).isEqualTo("redirect:/monitoring");
    }
}