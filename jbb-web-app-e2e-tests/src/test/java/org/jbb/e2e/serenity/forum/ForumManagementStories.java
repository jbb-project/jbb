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
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
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
        forumManagementSteps.should_be_informed_about_blank_category_name();
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
        forumManagementSteps.should_be_informed_about_blank_category_name();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void creating_forum_category_with_name_longer_than_255_characters_is_impossible() throws Exception {
        // given
        signInSteps.sign_in_as_administrator_with_success();

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_new_forum_category();
        forumManagementSteps.type_forum_category_name(getRandomString(256));
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
        forumManagementSteps.should_be_informed_about_blank_category_name();
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
        forumManagementSteps.should_be_informed_about_blank_category_name();
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
        forumManagementSteps.type_forum_category_name(getRandomString(256));
        forumManagementSteps.save_forum_category_form();

        // then
        forumManagementSteps.should_be_informed_about_incorrect_category_name_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void editing_forum_category_name_is_possible() throws Exception {
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

        make_rollback_after_test_case(
                delete_testbed_categories(fooCategoryName),
                delete_testbed_categories(barCategoryName)
        );

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
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void moving_down_forum_category_is_possible() throws Exception {
        // given
        String fooCategoryName = "foo category";
        String barCategoryName = "bar category";

        make_rollback_after_test_case(
                delete_testbed_categories(fooCategoryName),
                delete_testbed_categories(barCategoryName)
        );

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
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void removing_forum_category_with_forums_is_possible() throws Exception {
        // given
        String categoryName = "category to remove";
        String forumName = "forum to remove";

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);
        forumManagementSteps.create_forum(categoryName, forumName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_delete_category(categoryName);
        forumManagementSteps.confirm_delete_category();

        // then
        forumManagementSteps.category_should_not_be_visible_in_acp(categoryName);
        forumManagementSteps.forum_should_not_be_visible_in_acp_in_given_category(forumName, categoryName);
        homeSteps.opens_home_page();
        homeSteps.forum_category_should_not_be_visible(categoryName);
        homeSteps.forum_should_not_be_visible_in_given_category(forumName, categoryName);
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void removing_forum_category_and_moving_forums_to_another_category_is_possible() throws Exception {
        // given
        String categoryName = "category to remove";
        String anotherCategoryName = "another category";
        String forumName = "rescue this forum";
        make_rollback_after_test_case(delete_testbed_categories(anotherCategoryName));

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);
        forumManagementSteps.create_forum_category(anotherCategoryName);
        forumManagementSteps.create_forum(categoryName, forumName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_delete_category(categoryName);
        forumManagementSteps.choose_option_for_moving_forums_to_another_category(anotherCategoryName);
        forumManagementSteps.confirm_delete_category();

        // then
        forumManagementSteps.category_should_not_be_visible_in_acp(categoryName);
        forumManagementSteps.forum_should_be_visible_in_acp_in_given_category(forumName, anotherCategoryName);
        homeSteps.opens_home_page();
        homeSteps.forum_category_should_not_be_visible(categoryName);
        homeSteps.forum_should_be_visible_in_given_category(forumName, anotherCategoryName);
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void creating_forum_with_empty_name_is_impossible() throws Exception {
        // given
        String categoryName = "testbed category";
        make_rollback_after_test_case(delete_testbed_categories(categoryName));

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_new_forum();
        forumManagementSteps.type_forum_name("");
        forumManagementSteps.choose_forum_category_for_forum(categoryName);
        forumManagementSteps.save_forum_form();

        // then
        forumManagementSteps.should_be_informed_about_blank_forum_name();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void creating_forum_with_name_consists_of_whitespaces_is_impossible() throws Exception {
        // given
        String categoryName = "testbed category";
        make_rollback_after_test_case(delete_testbed_categories(categoryName));

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_new_forum();
        forumManagementSteps.type_forum_name("        ");
        forumManagementSteps.choose_forum_category_for_forum(categoryName);
        forumManagementSteps.save_forum_form();

        // then
        forumManagementSteps.should_be_informed_about_blank_forum_name();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void creating_forum_with_name_longer_than_255_characters_is_impossible() throws Exception {
        // given
        String categoryName = "testbed category";
        make_rollback_after_test_case(delete_testbed_categories(categoryName));

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_new_forum();
        forumManagementSteps.type_forum_name(getRandomString(256));
        forumManagementSteps.choose_forum_category_for_forum(categoryName);
        forumManagementSteps.save_forum_form();

        // then
        forumManagementSteps.should_be_informed_about_incorrect_forum_name_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void creating_forum_with_correct_name_is_possible() throws Exception {
        // given
        String categoryName = "testbed category";
        String forumName = "new testbed forum";
        String forumDescription = "forum description";
        make_rollback_after_test_case(delete_testbed_categories(categoryName));

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_new_forum();
        forumManagementSteps.type_forum_name(forumName);
        forumManagementSteps.type_forum_description(forumDescription);
        forumManagementSteps.choose_forum_category_for_forum(categoryName);
        forumManagementSteps.save_forum_form();

        // then
        forumManagementSteps.forum_should_be_visible_in_acp_in_given_category(forumName, categoryName);
        forumManagementSteps.forum_description_should_be_visible_in_acp(forumName, forumDescription);
        homeSteps.opens_home_page();
        homeSteps.forum_should_be_visible_in_given_category(forumName, categoryName);
        homeSteps.forum_description_should_be_visible(forumName, forumDescription);
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void editing_forum_with_empty_name_is_impossible() throws Exception {
        // given
        String categoryName = "testbed category";
        String forumName = "forum name";
        make_rollback_after_test_case(delete_testbed_categories(categoryName));

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);
        forumManagementSteps.create_forum(categoryName, forumName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_edit_forum(forumName);
        forumManagementSteps.type_forum_name("");
        forumManagementSteps.save_forum_form();

        // then
        forumManagementSteps.should_be_informed_about_blank_forum_name();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void editing_forum_with_name_consists_of_whitespaces_is_impossible() throws Exception {
        // given
        String categoryName = "testbed category";
        String forumName = "forum name";
        make_rollback_after_test_case(delete_testbed_categories(categoryName));

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);
        forumManagementSteps.create_forum(categoryName, forumName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_edit_forum(forumName);
        forumManagementSteps.type_forum_name("             ");
        forumManagementSteps.save_forum_form();

        // then
        forumManagementSteps.should_be_informed_about_blank_forum_name();
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void editing_forum_with_name_longer_than_255_characters_is_impossible() throws Exception {
        // given
        String categoryName = "testbed category";
        String forumName = "forum name";
        make_rollback_after_test_case(delete_testbed_categories(categoryName));

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);
        forumManagementSteps.create_forum(categoryName, forumName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_edit_forum(forumName);
        forumManagementSteps.type_forum_name(getRandomString(256));
        forumManagementSteps.save_forum_form();

        // then
        forumManagementSteps.should_be_informed_about_incorrect_forum_name_length();
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void editing_forum_with_new_name_is_possible() throws Exception {
        // given
        String categoryName = "testbed category";
        String forumName = "forum name";
        make_rollback_after_test_case(delete_testbed_categories(categoryName));

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);
        forumManagementSteps.create_forum(categoryName, forumName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_edit_forum(forumName);
        forumManagementSteps.type_forum_name("new forum name");
        forumManagementSteps.save_forum_form();

        // then
        forumManagementSteps.forum_should_be_visible_in_acp_in_given_category("new forum name", categoryName);
        homeSteps.opens_home_page();
        homeSteps.forum_should_be_visible_in_given_category("new forum name", categoryName);
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void editing_forum_with_new_description_is_possible() throws Exception {
        // given
        String categoryName = "testbed category";
        String forumName = "forum name";
        make_rollback_after_test_case(delete_testbed_categories(categoryName));

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);
        forumManagementSteps.create_forum(categoryName, forumName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_edit_forum(forumName);
        forumManagementSteps.type_forum_description("new description");
        forumManagementSteps.save_forum_form();

        // then
        forumManagementSteps.forum_description_should_be_visible_in_acp(forumName, "new description");
        homeSteps.opens_home_page();
        homeSteps.forum_description_should_be_visible(forumName, "new description");
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void editing_forum_with_new_category_is_possible() throws Exception {
        // given
        String categoryName = "testbed category";
        String anotherCategoryName = "another category";
        String forumName = "forum name";

        make_rollback_after_test_case(
                delete_testbed_categories(categoryName),
                delete_testbed_categories(anotherCategoryName)
        );

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);
        forumManagementSteps.create_forum_category(anotherCategoryName);
        forumManagementSteps.create_forum(categoryName, forumName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_edit_forum(forumName);
        forumManagementSteps.choose_forum_category_for_forum(anotherCategoryName);
        forumManagementSteps.save_forum_form();

        // then
        forumManagementSteps.forum_should_be_visible_in_acp_in_given_category(forumName, anotherCategoryName);
        homeSteps.opens_home_page();
        homeSteps.forum_should_be_visible_in_given_category(forumName, anotherCategoryName);
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void editing_forum_by_closing_is_possible() throws Exception {
        // given
        String categoryName = "testbed category";
        String forumName = "forum name";
        make_rollback_after_test_case(delete_testbed_categories(categoryName));

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);
        forumManagementSteps.create_forum(categoryName, forumName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_edit_forum(forumName);
        forumManagementSteps.set_forum_close_status();
        forumManagementSteps.save_forum_form();

        // then
        forumManagementSteps.forum_close_icon_should_be_visible_in_acp(forumName);
        homeSteps.opens_home_page();
        homeSteps.forum_close_icon_should_be_visible(forumName);
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void editing_forum_by_unclosing_is_possible() throws Exception {
        // given
        String categoryName = "testbed category";
        String forumName = "forum name";
        make_rollback_after_test_case(delete_testbed_categories(categoryName));

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);
        forumManagementSteps.create_forum(categoryName, forumName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_edit_forum(forumName);
        forumManagementSteps.set_forum_open_status();
        forumManagementSteps.save_forum_form();

        // then
        forumManagementSteps.forum_open_icon_should_be_visible_in_acp(forumName);
        homeSteps.opens_home_page();
        homeSteps.forum_open_icon_should_be_visible(forumName);
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void moving_up_forum_is_possible() throws Exception {
        // given
        String categoryName = "testbed category";
        String firstForumName = "first forum";
        String secondForumName = "second forum";
        make_rollback_after_test_case(delete_testbed_categories(categoryName));

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);
        forumManagementSteps.create_forum(categoryName, firstForumName);
        forumManagementSteps.create_forum(categoryName, secondForumName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_move_up_forum(secondForumName);

        // then
        forumManagementSteps.forum_is_before(secondForumName, firstForumName);
        homeSteps.opens_home_page();
        homeSteps.given_forum_is_before(secondForumName, firstForumName);
    }

    @Test
    @WithTagValuesOf({Tags.Type.REGRESSION, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void moving_down_forum_is_possible() throws Exception {
        // given
        String categoryName = "testbed category";
        String firstForumName = "first forum";
        String secondForumName = "second forum";
        make_rollback_after_test_case(delete_testbed_categories(categoryName));

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);
        forumManagementSteps.create_forum(categoryName, firstForumName);
        forumManagementSteps.create_forum(categoryName, secondForumName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_move_down_forum(firstForumName);

        // then
        forumManagementSteps.forum_is_before(secondForumName, firstForumName);
        homeSteps.opens_home_page();
        homeSteps.given_forum_is_before(secondForumName, firstForumName);
    }

    @Test
    @WithTagValuesOf({Tags.Type.SMOKE, Tags.Feature.FORUM_MANAGEMENT, Tags.Release.VER_0_8_0})
    public void removing_forum_is_possible() throws Exception {
        // given
        String categoryName = "testbed category";
        String forumToRemoveName = "forum to remove";
        make_rollback_after_test_case(delete_testbed_categories(categoryName));

        signInSteps.sign_in_as_administrator_with_success();
        forumManagementSteps.create_forum_category(categoryName);
        forumManagementSteps.create_forum(categoryName, forumToRemoveName);

        // when
        forumManagementSteps.open_forum_management_page();
        forumManagementSteps.click_for_delete_forum(forumToRemoveName);
        forumManagementSteps.confirm_delete_forum();

        // then
        forumManagementSteps.forum_should_not_be_visible_in_acp_in_given_category(forumToRemoveName, categoryName);
        homeSteps.opens_home_page();
        homeSteps.forum_should_not_be_visible_in_given_category(forumToRemoveName, categoryName);
    }

    RollbackAction delete_testbed_categories(String categoryName) {
        return () -> {
            forumManagementSteps.delete_forum_category(categoryName);
        };
    }

    private String getRandomString(int length) {
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
            .filteredBy(CharacterPredicates.LETTERS).build();
        return randomStringGenerator.generate(length);
    }
}
