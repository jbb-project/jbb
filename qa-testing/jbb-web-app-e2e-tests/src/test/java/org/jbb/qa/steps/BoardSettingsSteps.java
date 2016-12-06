/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.qa.steps;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.qa.pages.AcpBoardSettingsPage;

public class BoardSettingsSteps extends ScenarioSteps {
    AcpBoardSettingsPage boardSettingsPage;

    @Step
    public void open_board_settings_page() {
        boardSettingsPage.open();
    }

    @Step
    public void type_board_name(String boardName) {
        boardSettingsPage.typeBoardName(boardName);
    }

    @Step
    public void should_be_informed_about_incorrect_empty_board_name() {
        boardSettingsPage.shouldContainInfoAboutIncorrectEmptyBoardName();
    }

    @Step
    public void send_board_settings_form() {
        boardSettingsPage.clickSaveButton();
    }

    @Step
    public void should_be_informed_about_incorrect_board_name_length() {
        boardSettingsPage.shouldContainInfoAboutIncorrectBoardNameLength();
    }

    @Step
    public void new_board_name_should_be_visible(String boardName) {
        boardSettingsPage.assertBoardName(boardName);
    }

    @Step
    public void type_date_format(String dateFormat) {
        boardSettingsPage.typeDateFormat(dateFormat);
    }

    @Step
    public void should_be_informed_about_incorrect_date_format() {
        boardSettingsPage.shouldContainInfoAboutIncorrectDateFormat();
    }

    @Step
    public void should_be_informed_about_saving_settings() {
        boardSettingsPage.containsInfoAboutSavingSettingsCorrectly();
    }

    @Step
    public void should_be_informed_about_empty_date_format() {
        boardSettingsPage.shouldContainInfoAboutIncorrectEmptyDateFormat();
    }

    @Step
    public void set_new_board_name_successfully(String boardName) {
        open_board_settings_page();
        type_board_name(boardName);
        send_board_settings_form();
        should_be_informed_about_saving_settings();
    }

    @Step
    public void set_new_date_format_successfully(String dateFormat) {
        open_board_settings_page();
        type_date_format(dateFormat);
        send_board_settings_form();
        should_be_informed_about_saving_settings();
    }
}
