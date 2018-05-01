/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.commons;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.assertj.core.api.Assertions;
import org.jbb.e2e.serenity.Utils;
import org.jbb.e2e.serenity.web.signin.SignInPage;

import static org.assertj.core.api.Assertions.assertThat;


public class HomeSteps extends ScenarioSteps {

    HomePage homePage;
    UcpPage ucpPage;

    @Step
    public void opens_home_page() {
        homePage.open();
    }

    @Step
    public void should_see_jbb_footer() {
        assertThat(homePage.footer_content()).contains("jBB v.");
    }

    @Step
    public void should_see_registration_link() {
        homePage.has_registration_link();
    }

    @Step
    public void click_registration_link() {
        homePage.click_on_registration_link();
    }

    @Step
    public void click_sign_in_link() {
        homePage.click_on_sign_in_link();
    }

    @Step
    public void should_move_to_sign_in_page() {
        Assertions.assertThat(Utils.current_url()).endsWith(SignInPage.URL);
    }

    @Step
    public void open_main_ucp_page() {
        ucpPage.open();
    }

    @Step
    public void should_see_sign_in_page() {
        assertThat(Utils.current_url()).contains("signin");
    }

    @Step
    public void should_not_see_acp_link() {
        homePage.should_not_contain_acp_link();
    }

    @Step
    public void should_see_acp_link() {
        homePage.should_contain_acp_link();
    }

    @Step
    public void forum_category_should_be_visible(String categoryName) {
        homePage.forum_category_should_be_visible(categoryName);
    }

    @Step
    public void forum_category_should_not_be_visible(String categoryName) {
        homePage.forum_category_should_not_be_visible(categoryName);
    }

    @Step
    public void given_forum_category_is_before(String firstCategoryName, String secondCategoryName) {
        homePage.given_forum_category_is_before(firstCategoryName, secondCategoryName);
    }

    @Step
    public void forum_should_be_visible_in_given_category(String forumName, String categoryName) {
        homePage.forum_should_be_visible_in_given_category(forumName, categoryName);
    }

    @Step
    public void forum_description_should_be_visible(String forumName, String forumDescription) {
        homePage.forum_description_should_be_visible(forumName, forumDescription);
    }

    @Step
    public void forum_close_icon_should_be_visible(String forumName) {
        homePage.forum_close_icon_should_be_visible(forumName);
    }

    @Step
    public void forum_open_icon_should_be_visible(String forumName) {
        homePage.forum_open_icon_should_be_visible(forumName);
    }

    @Step
    public void given_forum_is_before(String firstForumName, String secondForumName) {
        homePage.given_forum_is_before(firstForumName, secondForumName);
    }

    @Step
    public void forum_should_not_be_visible_in_given_category(String forumName, String categoryName) {
        homePage.forum_should_not_be_visible_in_given_category(forumName, categoryName);
    }
}
