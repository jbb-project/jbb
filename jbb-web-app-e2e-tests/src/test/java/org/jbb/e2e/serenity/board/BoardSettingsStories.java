/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.board;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.JbbBaseSerenityStories;
import org.jbb.e2e.serenity.Tags;
import org.jbb.e2e.serenity.commons.AcpSteps;
import org.jbb.e2e.serenity.signin.SignInSteps;
import org.junit.Test;

public class BoardSettingsStories extends JbbBaseSerenityStories {

    @Steps
    SignInSteps signInSteps;
    @Steps
    AcpSteps acpSteps;
    @Steps
    BoardSettingsSteps boardSettingsSteps;

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_board_profile_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_board_settings_option();
        boardSettingsSteps.type_board_name("");
        boardSettingsSteps.send_board_settings_form();

        // then
        boardSettingsSteps.should_be_informed_about_incorrect_empty_board_name();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_board_profile_to_whitespace_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_board_settings_option();
        boardSettingsSteps.type_board_name("                 ");
        boardSettingsSteps.send_board_settings_form();

        // then
        boardSettingsSteps.should_be_informed_about_incorrect_empty_board_name();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_board_profile_longer_than_60_characters_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_board_settings_option();
        boardSettingsSteps.type_board_name("0123456789012345678901234567890123456789012345678901234567891");
        boardSettingsSteps.send_board_settings_form();

        // then
        boardSettingsSteps.should_be_informed_about_incorrect_board_name_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_board_profile_to_new_value_is_possible() throws Exception {
        make_rollback_after_test_case(restore_default_board_settings());

        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        boardSettingsSteps.set_new_board_name_successfully("jBB Board New");

        // then
        boardSettingsSteps.new_board_name_should_be_visible("jBB Board New");
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_date_format_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_board_settings_option();
        boardSettingsSteps.type_date_format("");
        boardSettingsSteps.send_board_settings_form();

        // then
        boardSettingsSteps.should_be_informed_about_empty_date_format();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_date_format_to_whitespace_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_board_settings_option();
        boardSettingsSteps.type_date_format("      ");
        boardSettingsSteps.send_board_settings_form();

        // then
        boardSettingsSteps.should_be_informed_about_empty_date_format();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_date_format_to_incorrect_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_board_settings_option();
        boardSettingsSteps.type_date_format("DLdlo HH:RR lor");
        boardSettingsSteps.send_board_settings_form();

        // then
        boardSettingsSteps.should_be_informed_about_incorrect_date_format();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_date_format_to_new_value_is_possible() throws Exception {
        make_rollback_after_test_case(restore_default_board_settings());

        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_board_settings_option();
        boardSettingsSteps.type_date_format("dd.MM.yyyy,HH:mm:ss");
        boardSettingsSteps.send_board_settings_form();

        // then
        boardSettingsSteps.should_be_informed_about_saving_settings();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_9_0})
    public void update_duration_format_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_board_settings_option();
        boardSettingsSteps.type_duration_format("");
        boardSettingsSteps.send_board_settings_form();

        // then
        boardSettingsSteps.should_be_informed_about_empty_duration_format();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_9_0})
    public void update_duration_format_to_whitespace_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_board_settings_option();
        boardSettingsSteps.type_duration_format("      ");
        boardSettingsSteps.send_board_settings_form();

        // then
        boardSettingsSteps.should_be_informed_about_empty_duration_format();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_9_0})
    public void update_duration_format_to_incorrect_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_board_settings_option();
        boardSettingsSteps.type_duration_format("DLdlo HH:RR lor");
        boardSettingsSteps.send_board_settings_form();

        // then
        boardSettingsSteps.should_be_informed_about_incorrect_duration_format();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.BOARD_SETTINGS, Tags.Release.VER_0_9_0})
    public void update_duration_format_to_new_value_is_possible() throws Exception {
        make_rollback_after_test_case(restore_default_board_settings());

        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_general_tab();
        acpSteps.choose_board_settings_option();
        boardSettingsSteps.type_duration_format("HH 'hours', mm 'minutes', ss 'seconds', S 'miliseconds'");
        boardSettingsSteps.send_board_settings_form();

        // then
        boardSettingsSteps.should_be_informed_about_saving_settings();
    }

    RollbackAction restore_default_board_settings() {
        return () -> {
            boardSettingsSteps.set_new_board_name_successfully("jBB Board");
            boardSettingsSteps.set_new_date_format_successfully("dd/MM/yyyy HH:mm:ss");
            boardSettingsSteps.set_new_duration_format_successfully("HH:mm:ss");
        };
    }
}
