/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.qa.pages;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultUrl(HomePage.URL)
public class HomePage extends PageObject {
    public static final String URL = "/";

    @FindBy(className = "footer")
    WebElement footer;

    @FindBys({@FindBy(linkText = "Sign up")})
    WebElement signUpLink;

    @FindBys({@FindBy(linkText = "Login")})
    WebElement loginLink;

    @FindBy(id = "displayedName")
    WebElement displayedNameText;

    public String footer_content() {
        return footer.findElement(By.tagName("p")).getText();
    }

    public void has_registration_link() {
        assertThat(signUpLink.isDisplayed()).isTrue();
    }

    public void click_on_registration_link() {
        signUpLink.click();
    }

    public void click_on_sign_in_link() {
        loginLink.click();
    }

    public String displayedName() {
        return displayedNameText.getText();
    }
}
