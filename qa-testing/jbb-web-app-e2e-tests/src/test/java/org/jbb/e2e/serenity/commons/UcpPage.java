/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.commons;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

@DefaultUrl(UcpPage.URL)
public class UcpPage extends PageObject {
    public static final String URL = "/ucp";

    @FindBys({@FindBy(linkText = "Profile")})
    WebElement profileTabLink;

    @FindBys({@FindBy(linkText = "Edit profile")})
    WebElement editProfileSubtabLink;

    @FindBys({@FindBy(linkText = "Edit account settings")})
    WebElement editAccountSubtabLink;

    public void click_on_profile_tab() {
        profileTabLink.click();
    }

    public void click_on_edit_profile_option() {
        editProfileSubtabLink.click();
    }

    public void click_on_edit_account_option() {
        editAccountSubtabLink.click();
    }


}
