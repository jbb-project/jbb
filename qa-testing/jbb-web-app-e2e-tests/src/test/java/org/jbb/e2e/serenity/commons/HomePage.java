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

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@DefaultUrl(HomePage.URL)
public class HomePage extends PageObject {
    public static final String URL = "/";

    @FindBy(className = "footer")
    WebElement footer;

    @FindBys({@FindBy(linkText = "Sign up")})
    WebElement signUpLink;

    @FindBys({@FindBy(linkText = "Sign in")})
    WebElement signinLink;

    @FindBys({@FindBy(linkText = "ACP")})
    WebElement acpLink;

    @FindBy(id = "ucpText")
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
        signinLink.click();
    }

    public String displayedName() {
        return displayedNameText.getText();
    }

    public void should_not_contain_acp_link() {
        try {
            acpLink.getLocation();
        } catch (NoSuchElementException e) {
            // ok
            return;
        }
        fail("should not contain acp link");
    }

    public void should_contain_acp_link() {
        assertThat(acpLink.isDisplayed()).isTrue();
    }

    public void forum_category_should_be_visible(String categoryName) {
        getDriver().findElement(By.xpath(String.format("//table/thead/tr/th[contains(text(),'%s')]", categoryName)));
    }

    public void forum_category_should_not_be_visible(String categoryName) {
        try {
            getDriver().findElement(By.xpath(String.format("//table/thead/tr/th[contains(text(),'%s')]", categoryName)));
        } catch (NoSuchElementException e) {
            // ok
            return;
        }
        fail("Should not contain forum category");
    }

    public void given_forum_category_is_before(String firstCategoryName, String secondCategoryName) {
        List<WebElement> webElements = getDriver().findElements(By.xpath("//table/thead/tr/th"));
        List<String> categoriesNames = webElements.stream().map(webElement -> webElement.getText()).collect(Collectors.toList());
        assertThat(categoriesNames.indexOf(firstCategoryName)).isLessThan(categoriesNames.indexOf(secondCategoryName));
    }

    public void forum_should_be_visible_in_given_category(String forumName, String categoryName) {
        getDriver().findElement(By.xpath(String.format("//table/thead/tr/th[contains(text(),'%s')]", categoryName)))
                .findElement(By.xpath(String.format("../../../tbody/tr/td/a/h4[contains(text(),'%s')]", forumName)));
    }

    public void forum_description_should_be_visible(String forumName, String forumDescription) {
        getDriver().findElement(By.xpath(String.format("//table/tbody/tr/td/a/h4[contains(text(),'%s')]", forumName)))
                .findElement(By.xpath(String.format("../../p[contains(text(),'%s')]", forumDescription)));
    }

    public void forum_lock_status_should_be_visible(String forumName) {
        getDriver().findElement(By.xpath(String.format("//table/tbody/tr/td/a/h4[contains(text(),'%s')]", forumName)))
                .findElement(By.xpath("../../../td[1]/img[@src=\"/resources/images/deleted_message-40.png\"]"));
    }

    public void forum_unlock_status_should_be_visible(String forumName) {
        getDriver().findElement(By.xpath(String.format("//table/tbody/tr/td/a/h4[contains(text(),'%s')]", forumName)))
                .findElement(By.xpath("../../../td[1]/img[@src=\"/resources/images/message-40.png\"]"));
    }
}
