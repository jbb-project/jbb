/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.database;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;
import org.jbb.e2e.serenity.JbbBaseSerenityStories;
import org.jbb.e2e.serenity.Tags;
import org.jbb.e2e.serenity.commons.AcpSteps;
import org.jbb.e2e.serenity.signin.SignInSteps;
import org.junit.Ignore;
import org.junit.Test;

public class DatabaseSettingsStories extends JbbBaseSerenityStories {

    @Steps
    SignInSteps signInSteps;
    @Steps
    AcpSteps acpSteps;
    @Steps
    DatabaseSettingsSteps databaseSettingsSteps;

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_database_filename_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_database_filename("");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_empty_database_filename();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_minimum_idle_db_connections_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_minimum_amount_idle_db_connections("");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_invalid_minimum_idle_db_connections_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_minimum_idle_db_connections_to_text_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_minimum_amount_idle_db_connections("yyyy");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_invalid_minimum_idle_db_connections_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_minimum_idle_db_connections_to_negative_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_minimum_amount_idle_db_connections("-1");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_not_positive_minimum_idle_db_connections_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_minimum_idle_db_connections_to_zero_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_minimum_amount_idle_db_connections("0");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_not_positive_minimum_idle_db_connections_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_maximum_size_connection_pool_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_maximum_size_connection_pool("");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_invalid_maximum_size_connection_pool_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_maximum_size_connection_pool_to_text_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_maximum_size_connection_pool("yyyy");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_invalid_maximum_size_connection_pool_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_maximum_size_connection_pool_to_negative_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_maximum_size_connection_pool("-1");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_not_positive_maximum_size_connection_pool_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_maximum_size_connection_pool_to_zero_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_maximum_size_connection_pool("0");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_not_positive_maximum_size_connection_pool_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_connection_timeout_miliseconds_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_connection_timeout_miliseconds("");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_invalid_connection_timeout_miliseconds_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_connection_timeout_miliseconds_to_text_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_connection_timeout_miliseconds("yyyy");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_invalid_connection_timeout_miliseconds_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_connection_timeout_miliseconds_to_negative_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_connection_timeout_miliseconds("-1");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_negative_connection_timeout_miliseconds_value();
    }

    @Ignore
    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_9_0})
    public void update_connection_maximum_lifetime_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_connection_maximum_lifetime_miliseconds("");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_9_0})
    public void update_connection_maximum_lifetime_to_negative_value_is_impossible()
        throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_connection_maximum_lifetime_miliseconds("-10");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_negative_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_9_0})
    public void update_connection_maximum_lifetime_to_text_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_connection_maximum_lifetime_miliseconds("aaaa");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_9_0})
    public void update_connection_idle_timeout_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_connection_idle_timeout_miliseconds("");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_9_0})
    public void update_connection_idle_timeout_to_negative_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_connection_idle_timeout_miliseconds("-1");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_negative_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_9_0})
    public void update_connection_idle_timeout_to_text_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_connection_idle_timeout_miliseconds("aaaa");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_9_0})
    public void update_connection_validation_timeout_to_empty_value_is_impossible()
        throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_connection_validation_timeout_miliseconds("");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_9_0})
    public void update_connection_validation_timeout_to_negative_value_is_impossible()
        throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_connection_validation_timeout_miliseconds("-1");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_negative_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_9_0})
    public void update_connection_validation_timeout_to_text_value_is_impossible()
        throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_connection_validation_timeout_miliseconds("xdddd");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_9_0})
    public void update_connection_leak_detection_threshold_to_empty_value_is_impossible()
        throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_connection_leak_detection_threshold_miliseconds("");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_9_0})
    public void update_connection_leak_detection_threshold_to_negative_value_is_impossible()
        throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_connection_leak_detection_threshold_miliseconds("-1");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_negative_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_9_0})
    public void update_connection_leak_detection_threshold_to_text_value_is_impossible()
        throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseSettingsSteps.open_database_settings_page();
        databaseSettingsSteps.type_connection_leak_detection_threshold_miliseconds("pppp");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @Ignore
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void new_database_settings_should_be_activated_after_application_restart() throws Exception {
        make_rollback_after_test_case(restore_default_database_settings());

        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        acpSteps.open_acp();
        acpSteps.choose_system_tab();
        acpSteps.choose_database_settings_option();
        databaseSettingsSteps.type_database_filename("jbb-new.db");
        databaseSettingsSteps.type_minimum_amount_idle_db_connections("7");
        databaseSettingsSteps.type_maximum_size_connection_pool("20");
        databaseSettingsSteps.type_connection_timeout_miliseconds("100000");
        databaseSettingsSteps.send_database_settings_form();

        // then
        databaseSettingsSteps.should_be_informed_about_saving_settings();
    }

    public RollbackAction restore_default_database_settings() {
        return () -> {
            databaseSettingsSteps.open_database_settings_page();
            databaseSettingsSteps.type_database_filename("jbb-hsqldb-database.db");
            databaseSettingsSteps.type_minimum_amount_idle_db_connections("5");
            databaseSettingsSteps.type_maximum_size_connection_pool("10");
            databaseSettingsSteps.type_connection_timeout_miliseconds("15000");
            databaseSettingsSteps.send_database_settings_form();
        };
    }

}
