/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.cache;

import static org.jbb.e2e.serenity.Tags.Interface;
import static org.jbb.e2e.serenity.Tags.Type;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;
import org.jbb.e2e.serenity.Tags.Feature;
import org.jbb.e2e.serenity.Tags.Release;
import org.jbb.e2e.serenity.web.EndToEndWebStories;
import org.jbb.e2e.serenity.web.signin.SignInSteps;
import org.junit.Test;

public class CacheSettingsStories extends EndToEndWebStories {

    @Steps
    SignInSteps signInSteps;
    @Steps
    AcpCacheSettingsSteps cacheSettingsSteps;

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_server_group_name_to_empty_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_server_group_name("");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_that_value_must_be_not_blank();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_server_port_to_empty_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_server_port("");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_server_port_to_zero_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_server_port("0");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_that_value_must_be_positive_number();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_server_port_to_negative_number_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_server_port("-9999");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_that_value_must_be_positive_number();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_server_port_to_text_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_server_port("aaa");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_server_bootstrap_nodes_to_empty_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_server_bootstrap_nodes("");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_that_value_must_be_not_empty();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_client_group_name_to_empty_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_client_group_name("");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_that_value_must_be_not_blank();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_client_bootstrap_nodes_to_empty_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_client_bootstrap_nodes("");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_that_value_must_be_not_empty();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_client_attempt_limit_to_empty_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_client_attempt_limit("");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_client_attempt_limit_to_zero_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_client_attempt_limit("0");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_that_value_must_be_positive_number();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_client_attempt_limit_to_negative_number_is_impossible()
        throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_client_attempt_limit("-282");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_that_value_must_be_positive_number();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_client_attempt_limit_to_text_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_client_attempt_limit("duudud");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_client_connection_attempt_period_to_empty_is_impossible()
        throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_client_attempt_period("");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_client_connection_attempt_period_to_zero_is_impossible()
        throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_client_attempt_period("0");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_that_value_must_be_positive_number();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_client_connection_attempt_period_to_negative_is_impossible()
        throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_client_attempt_period("-99");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_that_value_must_be_positive_number();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_client_connection_attempt_period_to_text_is_impossible()
        throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_client_attempt_period("aaaadiu");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_client_connection_timeout_to_empty_is_impossible()
        throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_client_connection_timeout("");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_about_invalid_value();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_client_connection_timeout_to_zero_is_impossible()
        throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_client_connection_timeout("0");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_that_value_must_be_positive_number();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_client_connection_timeout_to_negative_is_impossible()
        throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_client_connection_timeout("-271");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_that_value_must_be_positive_number();
    }

    @Test
    @WithTagValuesOf({Interface.WEB, Type.REGRESSION, Feature.CACHE_SETTINGS, Release.VER_0_9_0})
    public void update_hazelcast_client_connection_timeout_to_text_is_impossible()
        throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        cacheSettingsSteps.open_cache_settings();
        cacheSettingsSteps.type_hazelcast_client_connection_timeout("qmjieu");
        cacheSettingsSteps.save_cache_settings_form();

        // then
        cacheSettingsSteps.should_be_informed_about_invalid_value();
    }

}
