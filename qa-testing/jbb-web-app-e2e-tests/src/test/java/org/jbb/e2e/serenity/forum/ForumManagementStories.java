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

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;

import org.apache.commons.lang3.RandomStringUtils;
import org.jbb.e2e.serenity.JbbBaseSerenityStories;
import org.jbb.e2e.serenity.Tags;
import org.jbb.e2e.serenity.commons.HomeSteps;
import org.jbb.e2e.serenity.signin.SignInSteps;
import org.junit.Test;

public class ForumManagementStories extends JbbBaseSerenityStories {

    @Steps
    SignInSteps signInSteps;
    @Steps
    ForumManagementSteps forumManagementSteps;
    @Steps
    HomeSteps homeSteps;

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void creating_forum_category_with_empty_name_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_new_forum_category();
        forumManagementSteps.type_forum_category_name("");
        forumManagementSteps.save_forum_category_form();

        // then
        forumManagementSteps.should_be_informed_about_empty_category_name();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void creating_forum_category_with_name_consists_of_whitespaces_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_new_forum_category();
        forumManagementSteps.type_forum_category_name("          ");
        forumManagementSteps.save_forum_category_form();

        // then
        forumManagementSteps.should_be_informed_about_empty_category_name();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void creating_forum_category_with_name_longer_than_255_characters_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_new_forum_category();
        forumManagementSteps.type_forum_category_name(RandomStringUtils.randomAlphanumeric(256));
        forumManagementSteps.save_forum_category_form();

        // then
        forumManagementSteps.should_be_informed_about_incorrect_category_name_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void creating_forum_category_with_correct_name_length_is_possible() throws Exception {
        // given
        String categoryName = "New category for e2e testing";
        make_rollback_after_test_case(delete_testbed_categories(categoryName));
        signInSteps.sign_in_as_administrator_with_success();

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_new_forum_category();
        forumManagementSteps.type_forum_category_name(categoryName);
        forumManagementSteps.save_forum_category_form();

        // then
        forumManagementSteps.category_should_be_visible_in_acp(categoryName);
        homeSteps.opens_home_page();
        homeSteps.forum_category_should_be_visible(categoryName);
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void editing_forum_category_with_empty_name_is_impossible() throws Exception {
        // given
        String categoryName = "testing category for edit";
        make_rollback_after_test_case(delete_testbed_categories(categoryName));
        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_edit_category(categoryName);
        forumManagementSteps.type_forum_category_name("");
        forumManagementSteps.save_forum_category_form();

        // then
        forumManagementSteps.should_be_informed_about_empty_category_name();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void editing_forum_category_with_name_consists_of_whitespaces_is_impossible() throws Exception {
        // given
        String categoryName = "testing category for edit";
        make_rollback_after_test_case(delete_testbed_categories(categoryName));
        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_edit_category(categoryName);
        forumManagementSteps.type_forum_category_name("                     ");
        forumManagementSteps.save_forum_category_form();

        // then
        forumManagementSteps.should_be_informed_about_empty_category_name();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void editing_forum_category_with_name_longer_than_255_characters_is_impossible() throws Exception {
        // given
        String categoryName = "testing category for edit";
        make_rollback_after_test_case(delete_testbed_categories(categoryName));
        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_edit_category(categoryName);
        forumManagementSteps.type_forum_category_name(RandomStringUtils.randomAlphanumeric(256));
        forumManagementSteps.save_forum_category_form();

        // then
        forumManagementSteps.should_be_informed_about_incorrect_category_name_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void editing_forum_category_is_possible() throws Exception {
        // given
        String categoryName = "testing category for edit";
        String newCategoryName = "new name for testing category";
        make_rollback_after_test_case(delete_testbed_categories(newCategoryName));
        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_edit_category(categoryName);
        forumManagementSteps.type_forum_category_name(newCategoryName);
        forumManagementSteps.save_forum_category_form();

        // then
        forumManagementSteps.category_should_be_visible_in_acp(newCategoryName);
        homeSteps.opens_home_page();
        homeSteps.forum_category_should_be_visible(newCategoryName);
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void moving_up_forum_category_is_possible() throws Exception {
        // given
        String fooCategoryName = "foo category";
        String barCategoryName = "bar category";

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(fooCategoryName);
        forumManagementSteps.create_forum_category(barCategoryName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_move_up_category(barCategoryName);

        // then
        forumManagementSteps.category_is_before(barCategoryName, fooCategoryName);
        homeSteps.opens_home_page();
        homeSteps.given_forum_category_is_before(barCategoryName, fooCategoryName);

        // rollback
        forumManagementSteps.delete_forum_category(fooCategoryName);
        forumManagementSteps.delete_forum_category(barCategoryName);
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void moving_down_forum_category_is_possible() throws Exception {
        // given
        String fooCategoryName = "FOO category";
        String barCategoryName = "BAR category";

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(fooCategoryName);
        forumManagementSteps.create_forum_category(barCategoryName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_move_down_category(fooCategoryName);

        // then
        forumManagementSteps.category_is_before(barCategoryName, fooCategoryName);
        homeSteps.opens_home_page();
        homeSteps.given_forum_category_is_before(barCategoryName, fooCategoryName);

        // rollback
        forumManagementSteps.delete_forum_category(fooCategoryName);
        forumManagementSteps.delete_forum_category(barCategoryName);
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void removing_forum_category_is_possible() throws Exception {
        // given
        String categoryName = "category to remove";
        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_delete_category(categoryName);
        forumManagementSteps.confirm_delete_category();

        // then
        forumManagementSteps.category_should_not_be_visible_in_acp(categoryName);
        homeSteps.opens_home_page();
        homeSteps.forum_category_should_not_be_visible(categoryName);
    }

    RollbackAction delete_testbed_categories(String categoryName) {
        return () -> {
            forumManagementSteps.delete_forum_category(categoryName);
        };
    }
}
