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

    @Step
    public void click_for_new_forum() {
        forumManagementPage.clickNewForumButton();
    }

    @Step
    public void type_forum_name(String forumName) {
        forumManagementPage.typeForumName(forumName);
    }

    @Step
    public void save_forum_form() {
        forumManagementPage.saveForm();
    }

    @Step
    public void should_be_informed_about_empty_forum_name() {
        forumManagementPage.shouldContainInfoAboutEmptyForumName();
    }

    @Step
    public void choose_forum_category_for_forum(String categoryName) {
        forumManagementPage.chooseCategoryForForum(categoryName);
    }

    @Step
    public void should_be_informed_about_incorrect_forum_name_length() {
        forumManagementPage.shouldContainInfoAboutIncorrectForumNameLength();
    }

    @Step
    public void type_forum_description(String forumDescription) {
        forumManagementPage.typeForumDescription(forumDescription);
    }

    @Step
    public void forum_should_be_visible_in_acp_in_given_category(String forumName, String categoryName) {
        forumManagementPage.shouldContainForumInGivenCategory(forumName, categoryName);
    }

    @Step
    public void forum_description_should_be_visible_in_acp(String forumName, String forumDescription) {
        forumManagementPage.shouldContainForumDescription(forumName, forumDescription);
    }

    @Step
    public void create_forum(String categoryName, String forumName) {
        open_forum_management_page();
        click_for_new_forum();
        type_forum_name(forumName);
        choose_forum_category_for_forum(categoryName);
        save_forum_form();
    }

    @Step
    public void click_for_edit_forum(String forumName) {
        forumManagementPage.clickEditForum(forumName);
    }

    @Step
    public void set_forum_close_status() {
        forumManagementPage.setForumCloseStatus(true);
    }

    @Step
    public void forum_close_icon_should_be_visible_in_acp(String forumName) {
        forumManagementPage.shouldContainCloseIconForForum(forumName);
    }

    @Step
    public void set_forum_open_status() {
        forumManagementPage.setForumCloseStatus(false);
    }

    @Step
    public void forum_open_icon_should_be_visible_in_acp(String forumName) {
        forumManagementPage.shouldContainOpenIconForForum(forumName);
    }

    @Step
    public void click_move_up_forum(String forumName) {
        forumManagementPage.clickMoveUpForum(forumName);
    }

    @Step
    public void click_move_down_forum(String forumName) {
        forumManagementPage.clickMoveDownForum(forumName);
    }

    @Step
    public void forum_is_before(String firstForumName, String secondForumName) {
        forumManagementPage.forumIsBefore(firstForumName, secondForumName);

    }
}
