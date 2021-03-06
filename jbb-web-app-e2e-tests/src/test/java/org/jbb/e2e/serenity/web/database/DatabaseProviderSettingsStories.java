/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.database;

import static org.jbb.e2e.serenity.Tags.Feature;
import static org.jbb.e2e.serenity.Tags.Interface;
import static org.jbb.e2e.serenity.Tags.Release;
import static org.jbb.e2e.serenity.Tags.Type;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;
import org.jbb.e2e.serenity.web.EndToEndWebStories;
import org.jbb.e2e.serenity.web.signin.SignInSteps;
import org.junit.Test;

public class DatabaseProviderSettingsStories extends EndToEndWebStories {

    @Steps
    SignInSteps signInSteps;
    @Steps
    DatabaseProviderSettingsSteps databaseProviderSettingsSteps;

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.DATABASE_SETTINGS, Release.VER_0_9_0})
    public void update_h2_in_memory_database_name_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseProviderSettingsSteps.open_database_settings_page();
        databaseProviderSettingsSteps.type_h2_in_memory_database_name("");
        databaseProviderSettingsSteps.send_database_settings_form();

        // then
        databaseProviderSettingsSteps.should_be_informed_about_blank_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.DATABASE_SETTINGS, Release.VER_0_9_0})
    public void update_h2_embedded_server_database_name_to_empty_value_is_impossible()
            throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseProviderSettingsSteps.open_database_settings_page();
        databaseProviderSettingsSteps.type_h2_embedded_server_database_name("");
        databaseProviderSettingsSteps.send_database_settings_form();

        // then
        databaseProviderSettingsSteps.should_be_informed_about_blank_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.DATABASE_SETTINGS, Release.VER_0_9_0})
    public void update_h2_embedded_server_username_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseProviderSettingsSteps.open_database_settings_page();
        databaseProviderSettingsSteps.type_h2_embedded_server_username("");
        databaseProviderSettingsSteps.send_database_settings_form();

        // then
        databaseProviderSettingsSteps.should_be_informed_about_blank_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.DATABASE_SETTINGS, Release.VER_0_9_0})
    public void update_h2_managed_server_database_name_to_empty_value_is_impossible()
            throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseProviderSettingsSteps.open_database_settings_page();
        databaseProviderSettingsSteps.type_h2_managed_server_database_name("");
        databaseProviderSettingsSteps.send_database_settings_form();

        // then
        databaseProviderSettingsSteps.should_be_informed_about_blank_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.DATABASE_SETTINGS, Release.VER_0_9_0})
    public void update_h2_managed_server_port_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseProviderSettingsSteps.open_database_settings_page();
        databaseProviderSettingsSteps.type_h2_managed_server_port("");
        databaseProviderSettingsSteps.send_database_settings_form();

        // then
        databaseProviderSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.DATABASE_SETTINGS, Release.VER_0_9_0})
    public void update_h2_managed_server_port_to_zero_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseProviderSettingsSteps.open_database_settings_page();
        databaseProviderSettingsSteps.type_h2_managed_server_port("0");
        databaseProviderSettingsSteps.send_database_settings_form();

        // then
        databaseProviderSettingsSteps.should_be_informed_about_non_positive_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.DATABASE_SETTINGS, Release.VER_0_9_0})
    public void update_h2_managed_server_port_to_negative_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseProviderSettingsSteps.open_database_settings_page();
        databaseProviderSettingsSteps.type_h2_managed_server_port("-1");
        databaseProviderSettingsSteps.send_database_settings_form();

        // then
        databaseProviderSettingsSteps.should_be_informed_about_non_positive_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.DATABASE_SETTINGS, Release.VER_0_9_0})
    public void update_h2_managed_server_port_to_text_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseProviderSettingsSteps.open_database_settings_page();
        databaseProviderSettingsSteps.type_h2_managed_server_port("aaaaa");
        databaseProviderSettingsSteps.send_database_settings_form();

        // then
        databaseProviderSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.DATABASE_SETTINGS, Release.VER_0_9_0})
    public void update_h2_managed_server_username_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseProviderSettingsSteps.open_database_settings_page();
        databaseProviderSettingsSteps.type_h2_managed_server_username("");
        databaseProviderSettingsSteps.send_database_settings_form();

        // then
        databaseProviderSettingsSteps.should_be_informed_about_blank_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.DATABASE_SETTINGS, Release.VER_0_9_0})
    public void update_h2_remote_server_url_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseProviderSettingsSteps.open_database_settings_page();
        databaseProviderSettingsSteps.type_h2_remote_server_url("");
        databaseProviderSettingsSteps.send_database_settings_form();

        // then
        databaseProviderSettingsSteps.should_be_informed_about_blank_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.DATABASE_SETTINGS, Release.VER_0_9_0})
    public void update_h2_remote_server_username_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseProviderSettingsSteps.open_database_settings_page();
        databaseProviderSettingsSteps.type_h2_remote_server_username("");
        databaseProviderSettingsSteps.send_database_settings_form();

        // then
        databaseProviderSettingsSteps.should_be_informed_about_blank_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.DATABASE_SETTINGS,
        Release.VER_0_10_0})
    public void update_postgres_host_name_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseProviderSettingsSteps.open_database_settings_page();
        databaseProviderSettingsSteps.type_postgres_host_name("");
        databaseProviderSettingsSteps.send_database_settings_form();

        // then
        databaseProviderSettingsSteps.should_be_informed_about_blank_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.DATABASE_SETTINGS,
        Release.VER_0_10_0})
    public void update_postgres_server_port_to_zero_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseProviderSettingsSteps.open_database_settings_page();
        databaseProviderSettingsSteps.type_postgres_server_port("0");
        databaseProviderSettingsSteps.send_database_settings_form();

        // then
        databaseProviderSettingsSteps.should_be_informed_about_non_positive_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.DATABASE_SETTINGS,
        Release.VER_0_10_0})
    public void update_postgres_server_port_to_negative_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseProviderSettingsSteps.open_database_settings_page();
        databaseProviderSettingsSteps.type_postgres_server_port("-1");
        databaseProviderSettingsSteps.send_database_settings_form();

        // then
        databaseProviderSettingsSteps.should_be_informed_about_non_positive_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.DATABASE_SETTINGS,
        Release.VER_0_10_0})
    public void update_postgres_server_port_to_text_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseProviderSettingsSteps.open_database_settings_page();
        databaseProviderSettingsSteps.type_postgres_server_port("aaaa");
        databaseProviderSettingsSteps.send_database_settings_form();

        // then
        databaseProviderSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.DATABASE_SETTINGS,
        Release.VER_0_10_0})
    public void update_postgres_database_name_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseProviderSettingsSteps.open_database_settings_page();
        databaseProviderSettingsSteps.type_postgres_database_name("");
        databaseProviderSettingsSteps.send_database_settings_form();

        // then
        databaseProviderSettingsSteps.should_be_informed_about_blank_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.DATABASE_SETTINGS,
        Release.VER_0_10_0})
    public void update_postgres_username_to_empty_value_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        databaseProviderSettingsSteps.open_database_settings_page();
        databaseProviderSettingsSteps.type_postgres_username("");
        databaseProviderSettingsSteps.send_database_settings_form();

        // then
        databaseProviderSettingsSteps.should_be_informed_about_blank_value();
    }

}
