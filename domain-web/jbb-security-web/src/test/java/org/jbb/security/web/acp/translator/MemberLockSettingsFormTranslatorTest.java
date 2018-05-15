/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.acp.translator;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.security.api.lockout.MemberLockoutSettings;
import org.jbb.security.web.acp.form.MemberLockSettingsForm;
import org.junit.Test;

public class MemberLockSettingsFormTranslatorTest {

    private MemberLockSettingsFormTranslator translator = new MemberLockSettingsFormTranslator();

    @Test
    public void testMapping() throws Exception {
        // given
        MemberLockSettingsForm form = new MemberLockSettingsForm();
        form.setLockoutDurationMinutes(12L);
        form.setFailedSignInAttemptsExpirationMinutes(30L);
        form.setFailedAttemptsThreshold(100);
        form.setLockingEnabled(true);

        // when
        MemberLockoutSettings settings = translator.createSettingsModel(form);

        // then
        assertThat(settings).isNotNull();
        assertThat(settings.getLockoutDurationMinutes()).isEqualTo(12L);
        assertThat(settings.getFailedSignInAttemptsExpirationMinutes()).isEqualTo(30L);
        assertThat(settings.getFailedAttemptsThreshold()).isEqualTo(100);
        assertThat(settings.isLockingEnabled()).isTrue();

    }
}