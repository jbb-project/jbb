/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.board;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultUrl(AcpBoardSettingsPage.URL)
public class AcpBoardSettingsPage extends PageObject {
    public static final String URL = "/acp/general/board";

    @FindBy(className = "navbar-brand")
    WebElement boardNameInNavbar;

    @FindBy(id = "boardName")
    WebElement boardNameField;

    @FindBy(id = "dateFormat")
    WebElement dateFormatField;

    @FindBy(id = "durationFormat")
    WebElement durationFormatField;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Save')]")})
    WebElement saveButton;

    public void typeBoardName(String boardName) {
        boardNameField.clear();
        boardNameField.sendKeys(boardName);
    }

    public void shouldContainInfoAboutIncorrectEmptyBoardName() {
        shouldContainText("may not be empty");
    }

    public void clickSaveButton() {
        saveButton.click();
    }

    public void shouldContainInfoAboutIncorrectBoardNameLength() {
        shouldContainText("length must be between 1 and 60");
    }

    public void assertBoardName(String expectedBoardName) {
        assertThat(boardNameInNavbar.getText()).isEqualTo(expectedBoardName);
    }

    public void typeDateFormat(String dateFormat) {
        dateFormatField.clear();
        dateFormatField.sendKeys(dateFormat);
    }

    public void shouldContainInfoAboutIncorrectEmptyDateFormat() {
        shouldContainText("may not be empty");
    }

    public void shouldContainInfoAboutIncorrectDateFormat() {
        shouldContainText("Incorrect date format");
    }

    public void containsInfoAboutSavingSettingsCorrectly() {
        shouldContainText("Settings saved correctly");
    }

    public void typeDurationFormat(String durationFormat) {
        durationFormatField.clear();
        durationFormatField.sendKeys(durationFormat);
    }

    public void shouldContainInfoAboutIncorrectEmptyDurationFormat() {
        shouldContainText("may not be empty");
    }

    public void shouldContainInfoAboutIncorrectDurationFormat() {
        shouldContainText("Incorrect duration format");
    }
}
