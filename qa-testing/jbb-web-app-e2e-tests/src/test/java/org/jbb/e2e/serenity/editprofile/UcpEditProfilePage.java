/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.editprofile;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultUrl(UcpEditProfilePage.URL)
public class UcpEditProfilePage extends PageObject {
    public static final String URL = "/ucp/profile/edit";

    @FindBy(id = "displayedName")
    WebElement displayedNameField;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Save')]")})
    WebElement saveButton;

    @FindBy(id = "ucpText")
    WebElement displayedNameLink;

    public void typeDisplayedName(String displayedName) {
        displayedNameField.clear();
        displayedNameField.sendKeys(displayedName);
    }

    public void sendForm() {
        saveButton.click();
    }

    public void containsInfoAboutIncorrectDisplayedNameLength() {
        shouldContainText("size must be between 3 and 64");
    }


    public void containsInfoAboutSavingSettingsCorrectly() {
        shouldContainText("Your settings saved correctly");
    }

    public void containsDisplayedNameAsLinkToUcp(String displayedName) {
        assertThat(displayedNameLink.getText()).isEqualTo(displayedName);
    }
}
