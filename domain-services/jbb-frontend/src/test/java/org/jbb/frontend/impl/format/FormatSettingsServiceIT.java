/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.format;

import org.apache.commons.lang3.StringUtils;
import org.jbb.frontend.api.format.FormatException;
import org.jbb.frontend.api.format.FormatSettings;
import org.jbb.frontend.impl.BaseIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FormatSettingsServiceIT extends BaseIT {

    @Autowired
    private DefaultFormatSettingsService formatSettingsService;

    @Test(expected = FormatException.class)
    public void shouldThrowBoardException_whenNullDateFormatPassed() throws Exception {
        // given
        FormatSettings formatSettings = new FormatSettings();
        formatSettings.setDateFormat(null);
        formatSettings.setDurationFormat("HH:mm:ss");

        // when
        formatSettingsService.setFormatSettings(formatSettings);

        // then
        // throw FormatException
    }

    @Test(expected = FormatException.class)
    public void shouldThrowFormatException_whenEmptyDateFormatPassed() throws Exception {
        // given
        FormatSettings formatSettings = new FormatSettings();
        formatSettings.setDateFormat(StringUtils.EMPTY);
        formatSettings.setDurationFormat("HH:mm:ss");

        // when
        formatSettingsService.setFormatSettings(formatSettings);

        // then
        // throw FormatException
    }

    @Test(expected = FormatException.class)
    public void shouldThrowFormatException_whenIncorrectDateFormatPassed() throws Exception {
        // given
        FormatSettings formatSettings = new FormatSettings();
        formatSettings.setDateFormat("dd/MM/yyyy xHdolLH:mm:s");
        formatSettings.setDurationFormat("HH:mm:ss");

        // when
        formatSettingsService.setFormatSettings(formatSettings);

        // then
        // throw FormatException
    }

    @Test(expected = FormatException.class)
    public void shouldThrowFormatException_whenNullDurationFormatPassed() throws Exception {
        // given
        FormatSettings formatSettings = new FormatSettings();
        formatSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");
        formatSettings.setDurationFormat(null);

        // when
        formatSettingsService.setFormatSettings(formatSettings);

        // then
        // throw FormatException
    }

    @Test(expected = FormatException.class)
    public void shouldThrowFormatException_whenEmptyDurationFormatPassed() throws Exception {
        // given
        FormatSettings formatSettings = new FormatSettings();
        formatSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");
        formatSettings.setDurationFormat(StringUtils.EMPTY);

        // when
        formatSettingsService.setFormatSettings(formatSettings);

        // then
        // throw FormatException
    }

    @Test(expected = FormatException.class)
    public void shouldThrowFormatException_whenIncorrectDurationFormatPassed() throws Exception {
        // given
        FormatSettings formatSettings = new FormatSettings();
        formatSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");
        formatSettings.setDurationFormat("HH:mcmi@s!m:ss");

        // when
        formatSettingsService.setFormatSettings(formatSettings);

        // then
        // throw FormatException
    }

    @Test(expected = FormatException.class)
    public void shouldThrowFormatException_whenWhitespacesPassedAsDateFormat() throws Exception {
        // given
        FormatSettings formatSettings = new FormatSettings();
        formatSettings.setDateFormat("     ");
        formatSettings.setDurationFormat("HH:mm:ss");

        // when
        formatSettingsService.setFormatSettings(formatSettings);

        // then
        // throw FormatException
    }

    @Test(expected = FormatException.class)
    public void shouldThrowFormatException_whenWhitespacesPassedAsDurationFormat()
        throws Exception {
        // given
        FormatSettings formatSettings = new FormatSettings();
        formatSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");
        formatSettings.setDurationFormat("    ");

        // when
        formatSettingsService.setFormatSettings(formatSettings);

        // then
        // throw FormatException
    }
}