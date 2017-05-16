/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp.e2e.database;

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
public class Database_Settings_Stories {
    @Managed(uniqueSession = true)
    WebDriver driver;

    @Steps
    SignInSteps signInUser;
    @Steps
    UserInAcpSteps acpUser;
    @Steps
    DatabaseSettingsSteps databaseSettingsUser;

    private boolean rollbackNeeded = false;

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_database_filename_to_empty_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        databaseSettingsUser.open_database_settings_page();
        databaseSettingsUser.type_database_filename("");
        databaseSettingsUser.send_database_settings_form();

        // then
        databaseSettingsUser.should_be_informed_about_empty_database_filename();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_minimum_idle_db_connections_to_empty_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        databaseSettingsUser.open_database_settings_page();
        databaseSettingsUser.type_minimum_amount_idle_db_connections("");
        databaseSettingsUser.send_database_settings_form();

        // then
        databaseSettingsUser.should_be_informed_about_invalid_minimum_idle_db_connections_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_minimum_idle_db_connections_to_text_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        databaseSettingsUser.open_database_settings_page();
        databaseSettingsUser.type_minimum_amount_idle_db_connections("yyyy");
        databaseSettingsUser.send_database_settings_form();

        // then
        databaseSettingsUser.should_be_informed_about_invalid_minimum_idle_db_connections_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_minimum_idle_db_connections_to_negative_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        databaseSettingsUser.open_database_settings_page();
        databaseSettingsUser.type_minimum_amount_idle_db_connections("-1");
        databaseSettingsUser.send_database_settings_form();

        // then
        databaseSettingsUser.should_be_informed_about_not_positive_minimum_idle_db_connections_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_minimum_idle_db_connections_to_zero_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        databaseSettingsUser.open_database_settings_page();
        databaseSettingsUser.type_minimum_amount_idle_db_connections("0");
        databaseSettingsUser.send_database_settings_form();

        // then
        databaseSettingsUser.should_be_informed_about_not_positive_minimum_idle_db_connections_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_maximum_size_connection_pool_to_empty_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        databaseSettingsUser.open_database_settings_page();
        databaseSettingsUser.type_maximum_size_connection_pool("");
        databaseSettingsUser.send_database_settings_form();

        // then
        databaseSettingsUser.should_be_informed_about_invalid_maximum_size_connection_pool_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_maximum_size_connection_pool_to_text_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        databaseSettingsUser.open_database_settings_page();
        databaseSettingsUser.type_maximum_size_connection_pool("yyyy");
        databaseSettingsUser.send_database_settings_form();

        // then
        databaseSettingsUser.should_be_informed_about_invalid_maximum_size_connection_pool_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_maximum_size_connection_pool_to_negative_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        databaseSettingsUser.open_database_settings_page();
        databaseSettingsUser.type_maximum_size_connection_pool("-1");
        databaseSettingsUser.send_database_settings_form();

        // then
        databaseSettingsUser.should_be_informed_about_not_positive_maximum_size_connection_pool_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_maximum_size_connection_pool_to_zero_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        databaseSettingsUser.open_database_settings_page();
        databaseSettingsUser.type_maximum_size_connection_pool("0");
        databaseSettingsUser.send_database_settings_form();

        // then
        databaseSettingsUser.should_be_informed_about_not_positive_maximum_size_connection_pool_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_connection_timeout_miliseconds_to_empty_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        databaseSettingsUser.open_database_settings_page();
        databaseSettingsUser.type_connection_timeout_miliseconds("");
        databaseSettingsUser.send_database_settings_form();

        // then
        databaseSettingsUser.should_be_informed_about_invalid_connection_timeout_miliseconds_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_connection_timeout_miliseconds_to_text_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        databaseSettingsUser.open_database_settings_page();
        databaseSettingsUser.type_connection_timeout_miliseconds("yyyy");
        databaseSettingsUser.send_database_settings_form();

        // then
        databaseSettingsUser.should_be_informed_about_invalid_connection_timeout_miliseconds_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void update_connection_timeout_miliseconds_to_negative_value_is_impossible() throws Exception {
        // given
        signInAsAdministrator();

        // when
        databaseSettingsUser.open_database_settings_page();
        databaseSettingsUser.type_connection_timeout_miliseconds("-1");
        databaseSettingsUser.send_database_settings_form();

        // then
        databaseSettingsUser.should_be_informed_about_negative_connection_timeout_miliseconds_value();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.DATABASE_SETTINGS, Tags.Release.VER_0_6_0})
    public void new_database_settings_should_be_activated_after_application_restart() throws Exception {
        // mark rollback
        rollbackNeeded = true;

        // given
        signInAsAdministrator();

        // when
        acpUser.open_acp();
        acpUser.choose_system_tab();
        acpUser.choose_database_settings_option();
        databaseSettingsUser.type_database_filename("jbb-new.db");
        databaseSettingsUser.type_minimum_amount_idle_db_connections("7");
        databaseSettingsUser.type_maximum_size_connection_pool("20");
        databaseSettingsUser.type_connection_timeout_miliseconds("100000");
        databaseSettingsUser.send_database_settings_form();

        // then
        databaseSettingsUser.should_be_informed_about_saving_settings();
    }

    private void signInAsAdministrator() {
        signInUser.sign_in_with_credentials_with_success("administrator", "administrator", "Administrator");
    }

    @After
    public void tearDown() throws Exception {
        if (rollbackNeeded) {
            databaseSettingsUser.open_database_settings_page();
            databaseSettingsUser.type_database_filename("jbb-hsqldb-database.db");
            databaseSettingsUser.type_minimum_amount_idle_db_connections("5");
            databaseSettingsUser.type_maximum_size_connection_pool("10");
            databaseSettingsUser.type_connection_timeout_miliseconds("15000");
            databaseSettingsUser.send_database_settings_form();
        }
    }
}
