/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.membermanagement;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultUrl(AcpMemberBrowserPage.URL)
public class AcpMemberBrowserPage extends PageObject {
    public static final String URL = "/acp/members/manage";

    @FindBy(id = "username")
    WebElement usernameField;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Search')]")})
    WebElement searchButton;

    @FindBy(linkText = "Select")
    WebElement selectLink;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Delete member')]")})
    WebElement deleteMemberButton;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Remove lock')]")})
    WebElement removeLockButton;

    public void clickSearchButton() {
        searchButton.click();
    }

    public void typeUsername(String username) {
        usernameField.clear();
        usernameField.sendKeys(username);
    }

    public void clickSelect() {
        selectLink.click();
    }

    public void clickDeleteMemberButton() {
        deleteMemberButton.click();
    }

    public void shouldSeeInfoAboutEmptyResult() {
        assertThat(containsText("No members found with given criteria")).isTrue();
    }

    public void clickRemoveLockButton() {
        removeLockButton.click();
    }

    public void shouldSeeInfoAboutLockExpirationTimeDate(String date) {
        shouldContainText("Lock expiration time is " + date);
    }
}
