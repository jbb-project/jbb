/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.registration;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.jbb.e2e.serenity.Jbb_Base_Serenity_Stories;
import org.jbb.e2e.serenity.Tags;
import org.jbb.e2e.serenity.Utils;
import org.jbb.e2e.serenity.commons.HomeSteps;
import org.junit.Test;

import lombok.Builder;
import lombok.Getter;

import static org.assertj.core.api.Assertions.assertThat;

public class Registration_Stories extends Jbb_Base_Serenity_Stories {

    @Steps
    RegistrationSteps anonUser;
    @Steps
    HomeSteps anonUserAtHomePage;

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.REGISTRATION, Tags.Release.VER_0_3_0})
    public void registration_link_at_home_page_is_visible() throws Exception {
        // when
        anonUserAtHomePage.opens_home_page();

        // then
        anonUserAtHomePage.should_see_registration_link();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.REGISTRATION, Tags.Release.VER_0_3_0})
    public void registration_link_at_home_page_is_working() throws Exception {
        // when
        anonUserAtHomePage.opens_home_page();
        anonUserAtHomePage.click_registration_link();

        // then
        assertThat(Utils.current_url()).endsWith(RegistrationPage.URL);
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_3_0})
    public void registration_with_correct_data_is_possible() throws Exception {
        // when
        registerWith(Data.builder()
                        .username("mark")
                        .displayedName("Mark")
                        .email("mark@nokia.com")
                        .password("pa@ssw0rd")
                        .passwordAgain("pa@ssw0rd")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_registration_success();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_3_0})
    public void registration_with_username_shorter_than_3_characters_is_impossible() throws Exception {
        // when
        registerWith(Data.builder()
                        .username("jo")
                        .displayedName("Johnny")
                        .email("john@company.com")
                        .password("pa@ssw0rd")
                        .passwordAgain("pa@ssw0rd")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_incorrect_username_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_3_0})
    public void registration_with_username_longer_than_20_characters_is_impossible() throws Exception {
        // when
        registerWith(Data.builder()
                        .username("abcdefghijabcdefghija")
                        .displayedName("Johnny")
                        .email("john@company.com")
                        .password("pa@ssw0rd")
                        .passwordAgain("pa@ssw0rd")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_incorrect_username_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_3_0})
    public void registration_with_username_with_white_character_is_impossible() throws Exception {
        // when
        registerWith(Data.builder()
                        .username("my username")
                        .displayedName("Johnny")
                        .email("john@company.com")
                        .password("pa@ssw0rd")
                        .passwordAgain("pa@ssw0rd")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_forbidden_white_characters_in_username();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_3_0})
    public void registration_with_displayed_name_shorter_than_3_characters_is_impossible() throws Exception {
        // when
        registerWith(Data.builder()
                        .username("john")
                        .displayedName("Jo")
                        .email("john@company.com")
                        .password("pa@ssw0rd")
                        .passwordAgain("pa@ssw0rd")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_incorrect_display_name_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_3_0})
    public void registration_with_displayed_name_longer_than_64_characters_is_impossible() throws Exception {
        // when
        registerWith(Data.builder()
                        .username("john")
                        .displayedName("Johnny!!!!Johnny!!!!Johnny!!!!Johnny!!!!Johnny!!!!Johnny!!!!ABCDE")
                        .email("john@company.com")
                        .password("pa@ssw0rd")
                        .passwordAgain("pa@ssw0rd")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_incorrect_display_name_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_3_0})
    public void registration_with_incorrect_email_is_impossible() throws Exception {
        // when
        registerWith(Data.builder()
                        .username("john")
                        .displayedName("Johnny")
                        .email("john(AT)company.com")
                        .password("pa@ssw0rd")
                        .passwordAgain("pa@ssw0rd")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_incorrect_email();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_3_0})
    public void more_than_one_member_cannot_use_the_same_username() throws Exception {
        // when
        registerWith(Data.builder()
                        .username("admin")
                        .displayedName("Johnny")
                        .email("john@company.com")
                        .password("pa@ssw0rd")
                        .passwordAgain("pa@ssw0rd")
                        .build()
        );

        registerWith(Data.builder()
                        .username("admin")
                        .displayedName("Tom")
                        .email("tom@anothercompany.com")
                        .password("pa@ssw0rd")
                        .passwordAgain("pa@ssw0rd")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_busy_username();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_3_0})
    public void more_than_one_member_cannot_use_the_same_displayed_name() throws Exception {
        // when
        registerWith(Data.builder()
                        .username("john1992")
                        .displayedName("John")
                        .email("john@company.com")
                        .password("pa@ssw0rd")
                        .passwordAgain("pa@ssw0rd")
                        .build()
        );

        registerWith(Data.builder()
                        .username("johnny")
                        .displayedName("John")
                        .email("john@anothercompany.com")
                        .password("pa@ssw0rd")
                        .passwordAgain("pa@ssw0rd")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_busy_displayed_name();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_3_0})
    public void by_default_more_than_one_member_cannot_use_the_same_email() throws Exception {
        // when
        registerWith(Data.builder()
                        .username("john")
                        .displayedName("John")
                        .email("john@nsn.com")
                        .password("pa@ssw0rd")
                        .passwordAgain("pa@ssw0rd")
                        .build()
        );

        registerWith(Data.builder()
                        .username("johnny")
                        .displayedName("Johnny")
                        .email("john@nsn.com")
                        .password("pa@ssw0rd")
                        .passwordAgain("pa@ssw0rd")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_busy_email();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_3_0})
    public void registration_with_empty_password_is_impossible() throws Exception {
        // when
        registerWith(Data.builder()
                        .username("john")
                        .displayedName("Jo")
                        .email("john@company.com")
                        .password("")
                        .passwordAgain("")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_incorrect_length_of_password();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_4_0})
    public void by_default_registration_with_password_with_less_than_4_characters_is_impossible() throws Exception {
        // when
        registerWith(Data.builder()
                        .username("john")
                        .displayedName("John")
                        .email("john@nsn.com")
                        .password("abc")
                        .passwordAgain("abc")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_incorrect_length_of_password();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_4_0})
    public void by_default_registration_with_password_with_more_than_16_characters_is_impossible() throws Exception {
        // when
        registerWith(Data.builder()
                        .username("john")
                        .displayedName("John")
                        .email("john@nsn.com")
                        .password("abcdef1234567890X")
                        .passwordAgain("abcdef1234567890X")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_incorrect_length_of_password();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.REGISTRATION, Tags.Release.VER_0_4_0})
    public void registration_should_failed_when_user_passed_different_passwords() throws Exception {
        // when
        registerWith(Data.builder()
                        .username("john")
                        .displayedName("John")
                        .email("john@nsn.com")
                        .password("hibern@te1")
                        .passwordAgain("hibern@te2")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_not_match_passwords();
    }

    private void registerWith(Data data) {
        anonUser.open_registration_page();
        anonUser.type_username(data.getUsername());
        anonUser.type_displayed_name(data.getDisplayedName());
        anonUser.type_email(data.getEmail());
        anonUser.type_password(data.getPassword());
        anonUser.type_password_again(data.getPasswordAgain());
        anonUser.send_registration_form();
    }

    @Getter
    @Builder
    private static class Data {
        String username;
        String displayedName;
        String email;
        String password;
        String passwordAgain;
    }


}
