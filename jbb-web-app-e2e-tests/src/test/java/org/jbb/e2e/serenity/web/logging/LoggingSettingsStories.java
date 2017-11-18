/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.logging;

import static org.jbb.e2e.serenity.Tags.Feature;
import static org.jbb.e2e.serenity.Tags.Interface;
import static org.jbb.e2e.serenity.Tags.Release;
import static org.jbb.e2e.serenity.Tags.Type;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;
import org.jbb.e2e.serenity.web.EndToEndWebStories;
import org.jbb.e2e.serenity.web.signin.SignInSteps;
import org.junit.Test;

public class LoggingSettingsStories extends EndToEndWebStories {

    @Steps
    SignInSteps signInSteps;
    @Steps
    LoggingSettingsSteps loggingSettingsSteps;

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_console_appender_with_empty_name_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_console_appender();
        loggingSettingsSteps.type_console_appender_name("");
        loggingSettingsSteps.type_log_pattern("[%d{dd-MM-yyyy HH:mm:ss}] [%thread] %-5level --> [%c] %m%n");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_blank_field();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_console_appender_with_already_existing_name_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_console_appender();
        loggingSettingsSteps.type_console_appender_name("Default-jbb-log");
        loggingSettingsSteps.type_log_pattern("[%d{dd-MM-yyyy HH:mm:ss}] [%thread] %-5level --> [%c] %m%n");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_busy_appender_name();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_console_appender_with_empty_log_pattern_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_console_appender();
        loggingSettingsSteps.type_console_appender_name("new-console-appender");
        loggingSettingsSteps.type_log_pattern("");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_blank_field();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_console_appender_is_possible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_console_appender();
        loggingSettingsSteps.type_console_appender_name("new-console-appender");
        loggingSettingsSteps.type_log_pattern("[%d{dd-MM-yyyy HH:mm:ss}] [%thread] %-5level --> [%c] %m%n");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_info_about_success();
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.should_contain_appender_name("new-console-appender");
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void editing_console_appender_with_empty_log_pattern_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_edit_appender("Default-console");
        loggingSettingsSteps.type_log_pattern("");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_blank_field();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void removing_console_appender_is_possible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_console_appender();
        loggingSettingsSteps.type_console_appender_name("fordelete-console-appender");
        loggingSettingsSteps.type_log_pattern("[%d{dd-MM-yyyy HH:mm:ss}] [%thread] %-5level --> [%c] %m%n");
        loggingSettingsSteps.send_appender_form();
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_delete_appender("fordelete-console-appender");

        // then
        loggingSettingsSteps.should_not_contain_appender_name("fordelete-console-appender");
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_file_appender_with_empty_name_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_file_appender();
        type_correct_file_appender_details();
        loggingSettingsSteps.type_file_appender_name("");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_blank_field();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_file_appender_with_already_existing_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_file_appender();
        type_correct_file_appender_details();
        loggingSettingsSteps.type_file_appender_name("Default-console");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_busy_appender_name();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_file_appender_with_empty_log_pattern_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_file_appender();
        type_correct_file_appender_details();
        loggingSettingsSteps.type_log_pattern("");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_blank_field();
    }


    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_file_appender_with_empty_current_log_file_name_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_file_appender();
        type_correct_file_appender_details();
        loggingSettingsSteps.type_current_log_file_name("");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_blank_field();
    }


    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_file_appender_with_empty_rotation_file_name_pattern_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_file_appender();
        type_correct_file_appender_details();
        loggingSettingsSteps.type_rotation_file_name_pattern("");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_blank_field();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_file_appender_with_empty_max_file_size_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_file_appender();
        type_correct_file_appender_details();
        loggingSettingsSteps.type_max_size_file("");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_incorrect_max_file_size();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_file_appender_with_incorrect_max_file_size_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_file_appender();
        type_correct_file_appender_details();
        loggingSettingsSteps.type_max_size_file("100 fb");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_incorrect_max_file_size();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_file_appender_with_negative_max_history_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_file_appender();
        type_correct_file_appender_details();
        loggingSettingsSteps.type_max_history("-1");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_negative_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_file_appender_with_text_in_max_history_field_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_file_appender();
        type_correct_file_appender_details();
        loggingSettingsSteps.type_max_history("aaaaaaaaaa");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_file_appender_with_empty_max_history_field_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_file_appender();
        type_correct_file_appender_details();
        loggingSettingsSteps.type_max_history("");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_file_appender_is_possible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_file_appender();
        type_correct_file_appender_details();
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_info_about_success();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void editing_file_appender_with_empty_log_pattern_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_edit_appender("Default-jbb-log");
        loggingSettingsSteps.type_log_pattern("");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_blank_field();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void editing_file_appender_with_empty_current_log_file_name_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_edit_appender("Default-jbb-log");
        loggingSettingsSteps.type_current_log_file_name("");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_blank_field();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void editing_file_appender_with_empty_rotation_file_name_pattern_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_edit_appender("Default-jbb-log");
        loggingSettingsSteps.type_rotation_file_name_pattern("");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_blank_field();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void editing_file_appender_with_empty_max_size_file_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_edit_appender("Default-jbb-log");
        loggingSettingsSteps.type_max_size_file("");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_incorrect_max_file_size();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void editing_file_appender_with_incorrect_max_size_file_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_edit_appender("Default-jbb-log");
        loggingSettingsSteps.type_max_size_file("200 E");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_incorrect_max_file_size();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void editing_file_appender_with_empty_max_history_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_edit_appender("Default-jbb-log");
        loggingSettingsSteps.type_max_history("");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void editing_file_appender_with_text_max_history_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_edit_appender("Default-jbb-log");
        loggingSettingsSteps.type_max_history("aaaaaaaaaa");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void editing_file_appender_with_negative_max_history_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_edit_appender("Default-jbb-log");
        loggingSettingsSteps.type_max_history("-1");
        loggingSettingsSteps.send_appender_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_negative_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void removing_file_appender_is_possible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_file_appender();
        type_correct_file_appender_details();
        loggingSettingsSteps.type_file_appender_name("fordelete-file-appender");
        loggingSettingsSteps.send_appender_form();
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_delete_appender("fordelete-file-appender");

        // then
        loggingSettingsSteps.should_not_contain_appender_name("fordelete-file-appender");
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_logger_with_empty_name_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_logger();
        loggingSettingsSteps.type_logger_name("");
        loggingSettingsSteps.send_logger_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_blank_field();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_logger_with_incorrect_name_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_logger();
        loggingSettingsSteps.type_logger_name("this is not package or java class");
        loggingSettingsSteps.send_logger_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_incorrect_logger_name();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_logger_with_already_exists_name_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_logger();
        loggingSettingsSteps.type_logger_name("jdbc");
        loggingSettingsSteps.send_logger_form();

        // then
        loggingSettingsSteps.should_contain_warn_about_busy_logger_name();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void adding_logger_is_possible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_logger();
        loggingSettingsSteps.type_logger_name("org.jbb.testbed.AppContext");
        loggingSettingsSteps.send_logger_form();

        // then
        loggingSettingsSteps.should_contain_info_about_success();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void removing_logger_is_possible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.click_for_add_new_logger();
        loggingSettingsSteps.type_logger_name("org.jbb.todelete");
        loggingSettingsSteps.send_logger_form();
        loggingSettingsSteps.should_contain_info_about_success();

        loggingSettingsSteps.open_logging_settings_page();
        loggingSettingsSteps.should_contain_logger_name("org.jbb.todelete");

        loggingSettingsSteps.click_delete_logger("org.jbb.todelete");

        // then
        loggingSettingsSteps.should_not_contain_logger_name("org.jbb.todelete");
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.LOGGING_SETTINGS, Release.VER_0_7_0})
    public void removing_root_logger_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        loggingSettingsSteps.open_address_for_removing_root_logger();

        // then
        loggingSettingsSteps.should_contain_error_about_attempting_to_remove_root_logger();
    }

    private void type_correct_file_appender_details() {
        loggingSettingsSteps.type_file_appender_name("new-file-appender");
        loggingSettingsSteps.type_current_log_file_name("testbed.log");
        loggingSettingsSteps.type_rotation_file_name_pattern("testbed_%d{yyyy-MM-dd}_%i.log");
        loggingSettingsSteps.type_max_size_file("50 MB");
        loggingSettingsSteps.type_max_history("14");
        loggingSettingsSteps.type_log_pattern("[%d{dd-MM-yyyy HH:mm:ss}] [%thread] %-5level --> [%c] %m%n");
    }

}
