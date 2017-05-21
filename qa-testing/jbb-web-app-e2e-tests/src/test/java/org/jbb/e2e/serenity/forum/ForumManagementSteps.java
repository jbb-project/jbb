/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.forum;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

public class ForumManagementSteps extends ScenarioSteps {

    AcpForumManagementPage forumManagementPage;

    @Step
    public void open_forum_management_page() {
        forumManagementPage.open();
    }

    @Step
    public void click_for_new_forum_category() {
        forumManagementPage.clickNewForumCategoryButton();
    }

    @Step
    public void type_forum_category_name(String categoryName) {
        forumManagementPage.typeForumCategoryName(categoryName);
    }

    @Step
    public void save_forum_category_form() {
        forumManagementPage.saveForm();
    }

    @Step
    public void should_be_informed_about_empty_category_name() {
        forumManagementPage.shouldContainInfoAboutEmptyCategoryName();
    }

    @Step
    public void should_be_informed_about_incorrect_category_name_length() {
        forumManagementPage.shouldContainInfoAboutIncorrectCategoryNameLength();
    }

    @Step
    public void category_should_be_visible_in_acp(String categoryName) {
        forumManagementPage.shouldContainCategory(categoryName);
    }

    @Step
    public void category_should_not_be_visible_in_acp(String categoryName) {
        forumManagementPage.shouldNotContainCategory(categoryName);
    }

    @Step
    public void create_forum_category(String categoryName) {
        open_forum_management_page();
        click_for_new_forum_category();
        type_forum_category_name(categoryName);
        save_forum_category_form();
        category_should_be_visible_in_acp(categoryName);
    }

    @Step
    public void delete_forum_category(String categoryName) {
        open_forum_management_page();
        click_for_delete_category(categoryName);
        confirm_delete_category();
        category_should_not_be_visible_in_acp(categoryName);
    }

    @Step
    public void click_for_edit_category(String categoryName) {
        forumManagementPage.clickEditCategory(categoryName);
    }

    @Step
    public void click_move_up_category(String categoryName) {
        forumManagementPage.clickMoveUpCategory(categoryName);
    }

    @Step
    public void category_is_before(String firstCategoryName, String secondCategoryName) {
        forumManagementPage.categoryIsBefore(firstCategoryName, secondCategoryName);
    }

    @Step
    public void click_move_down_category(String categoryName) {
        forumManagementPage.clickMoveDownCategory(categoryName);
    }

    @Step
    public void click_for_delete_category(String categoryName) {
        forumManagementPage.clickDeleteCategory(categoryName);
    }

    @Step
    public void confirm_delete_category() {
        forumManagementPage.clickDeleteButton();
    }
}