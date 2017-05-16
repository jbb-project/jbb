/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp.e2e.board;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.webapp.e2e.Tags;
import org.jbb.webapp.e2e.commons.UserInAcpSteps;
import org.jbb.webapp.e2e.signin.SignInSteps;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityRunner.class)
public class Board_Settings_Stories {
    @Managed(uniqueSession = true)
    WebDriver driver;
    @Steps
    SignInSteps signInUser;
    @Steps
    UserInAcpSteps acpUser;
    @Steps
    BoardSettingsSteps boardSettingsUser;
    private boolean rollbackNeeded = false;

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_board_profile_to_empty_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        acpUser.open_acp();
        acpUser.choose_general_tab();
        acpUser.choose_board_settings_option();
        boardSettingsUser.type_board_name("");
        boardSettingsUser.send_board_settings_form();

        // then
        boardSettingsUser.should_be_informed_about_incorrect_empty_board_name();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_board_profile_to_whitespace_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        acpUser.open_acp();
        acpUser.choose_general_tab();
        acpUser.choose_board_settings_option();
        boardSettingsUser.type_board_name("                 ");
        boardSettingsUser.send_board_settings_form();

        // then
        boardSettingsUser.should_be_informed_about_incorrect_empty_board_name();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_board_profile_longer_than_60_characters_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        acpUser.open_acp();
        acpUser.choose_general_tab();
        acpUser.choose_board_settings_option();
        boardSettingsUser.type_board_name("0123456789012345678901234567890123456789012345678901234567891");
        boardSettingsUser.send_board_settings_form();

        // then
        boardSettingsUser.should_be_informed_about_incorrect_board_name_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_board_profile_to_new_value_is_possible() throws Exception {
        // mark rollback
        rollbackNeeded = true;

        // given
        signInAsAdministrator();

        // when
        boardSettingsUser.set_new_board_name_successfully("jBB Board New");

        // then
        boardSettingsUser.new_board_name_should_be_visible("jBB Board New");
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_date_format_to_empty_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        acpUser.open_acp();
        acpUser.choose_general_tab();
        acpUser.choose_board_settings_option();
        boardSettingsUser.type_date_format("");
        boardSettingsUser.send_board_settings_form();

        // then
        boardSettingsUser.should_be_informed_about_empty_date_format();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_date_format_to_whitespace_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        acpUser.open_acp();
        acpUser.choose_general_tab();
        acpUser.choose_board_settings_option();
        boardSettingsUser.type_date_format("      ");
        boardSettingsUser.send_board_settings_form();

        // then
        boardSettingsUser.should_be_informed_about_empty_date_format();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_date_format_to_incorrect_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        acpUser.open_acp();
        acpUser.choose_general_tab();
        acpUser.choose_board_settings_option();
        boardSettingsUser.type_date_format("DLdlo HH:RR lor");
        boardSettingsUser.send_board_settings_form();

        // then
        boardSettingsUser.should_be_informed_about_incorrect_date_format();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_date_format_to_new_value_is_possible() throws Exception {
        // mark rollback
        rollbackNeeded = true;

        // given
        signInAsAdministrator();

        // when
        acpUser.open_acp();
        acpUser.choose_general_tab();
        acpUser.choose_board_settings_option();
        boardSettingsUser.type_date_format("dd.MM.yyyy,HH:mm:ss");
        boardSettingsUser.send_board_settings_form();

        // then
        boardSettingsUser.should_be_informed_about_saving_settings();
    }

    private void signInAsAdministrator() {
        signInUser.sign_in_with_credentials_with_success("administrator", "administrator", "Administrator");
    }

    @After
    public void tearDown() throws Exception {
        if (rollbackNeeded) {
            boardSettingsUser.set_new_board_name_successfully("jBB Board");
            boardSettingsUser.set_new_date_format_successfully("dd/MM/yyyy HH:mm:ss");
        }
    }
}
