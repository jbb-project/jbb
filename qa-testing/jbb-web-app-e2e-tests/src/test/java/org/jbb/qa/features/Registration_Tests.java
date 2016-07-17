/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.qa.features;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;

import org.jbb.qa.steps.AnonUserSteps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import lombok.Builder;
import lombok.Getter;

@RunWith(SerenityRunner.class)
public class Registration_Tests {
    @Managed(uniqueSession = true)
    WebDriver driver;

    @Steps
    AnonUserSteps anonUser;

    @Test
    public void registration_link_at_home_page_is_visible() throws Exception {
        // when
        anonUser.opens_home_page();

        // then
        anonUser.should_see_registration_link();
    }

    @Test
    public void registration_with_correct_data_is_possible() throws Exception {
        // when
        registerWith(Data.builder()
                        .login("mark")
                        .displayedName("Mark")
                        .email("mark@nokia.com")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_registration_success();
    }

    @Test
    public void registration_with_login_shorter_than_3_characters_is_impossible() throws Exception {
        // when
        registerWith(Data.builder()
                        .login("jo")
                        .displayedName("Johnny")
                        .email("john@company.com")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_incorrect_login_length();
    }

    @Test
    public void registration_with_login_longer_than_20_characters_is_impossible() throws Exception {
        // when
        registerWith(Data.builder()
                        .login("abcdefghijabcdefghija")
                        .displayedName("Johnny")
                        .email("john@company.com")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_incorrect_login_length();
    }

    @Test
    public void registration_with_login_with_white_character_is_impossible() throws Exception {
        // when
        registerWith(Data.builder()
                        .login("my login")
                        .displayedName("Johnny")
                        .email("john@company.com")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_forbidden_white_characters_in_login();
    }

    @Test
    public void registration_with_displayed_name_shorter_than_3_characters_is_impossible() throws Exception {
        // when
        registerWith(Data.builder()
                        .login("john")
                        .displayedName("Jo")
                        .email("john@company.com")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_incorrect_display_name_length();
    }

    @Test
    public void registration_with_displayed_name_longer_than_64_characters_is_impossible() throws Exception {
        // when
        registerWith(Data.builder()
                        .login("john")
                        .displayedName("Johnny!!!!Johnny!!!!Johnny!!!!Johnny!!!!Johnny!!!!Johnny!!!!ABCDE")
                        .email("john@company.com")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_incorrect_display_name_length();
    }

    @Test
    public void registration_with_incorrect_email_is_impossible() throws Exception {
        // when
        registerWith(Data.builder()
                        .login("john")
                        .displayedName("Johnny")
                        .email("john(AT)company.com")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_incorrect_email();
    }

    @Test
    public void more_than_one_member_cannot_use_the_same_login() throws Exception {
        // when
        registerWith(Data.builder()
                        .login("admin")
                        .displayedName("Johnny")
                        .email("john@company.com")
                        .build()
        );

        registerWith(Data.builder()
                        .login("admin")
                        .displayedName("Tom")
                        .email("tom@anothercompany.com")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_busy_login();
    }

    @Test
    public void more_than_one_member_cannot_use_the_same_displayed_name() throws Exception {
        // when
        registerWith(Data.builder()
                        .login("john1992")
                        .displayedName("John")
                        .email("john@company.com")
                        .build()
        );

        registerWith(Data.builder()
                        .login("johnny")
                        .displayedName("John")
                        .email("john@anothercompany.com")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_busy_displayed_name();
    }

    @Test
    public void by_default_more_than_one_member_cannot_use_the_same_email() throws Exception {
        // when
        registerWith(Data.builder()
                        .login("john")
                        .displayedName("John")
                        .email("john@nsn.com")
                        .build()
        );

        registerWith(Data.builder()
                        .login("johnny")
                        .displayedName("Johnny")
                        .email("john@nsn.com")
                        .build()
        );

        // then
        anonUser.should_be_informed_about_busy_email();
    }

    private void registerWith(Data data) {
        anonUser.opens_registration_page();
        anonUser.type_login(data.getLogin());
        anonUser.type_displayed_name(data.getDisplayedName());
        anonUser.type_email(data.getEmail());
        anonUser.send_registration_form();
    }

    @Getter
    @Builder
    private static class Data {
        String login;
        String displayedName;
        String email;
    }


}
