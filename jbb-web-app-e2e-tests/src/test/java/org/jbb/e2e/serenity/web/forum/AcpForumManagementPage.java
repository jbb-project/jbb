/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.forum;

import net.serenitybdd.core.annotations.findby.By;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@DefaultUrl(AcpForumManagementPage.URL)
public class AcpForumManagementPage extends PageObject {
    public static final String URL = "/acp/general/forums";

    @FindBys({@FindBy(linkText = "New forum category")})
    WebElement newForumCategoryButton;

    @FindBys({@FindBy(linkText = "New forum")})
    WebElement newForumButton;

    @FindBy(id = "forumName")
    WebElement forumNameField;

    @FindBy(id = "categoryName")
    WebElement categoryNameField;

    @FindBy(id = "description")
    WebElement forumDescriptionTextArea;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Save')]")})
    WebElement saveButton;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Delete')]")})
    WebElement deleteButton;

    @FindBy(id = "category")
    WebElement categorySelect;

    @FindBy(id = "closed")
    WebElement forumClosedCheckbox;

    public void clickNewForumCategoryButton() {
        newForumCategoryButton.click();
    }

    public void typeForumCategoryName(String categoryName) {
        categoryNameField.clear();
        categoryNameField.sendKeys(categoryName);
    }

    public void saveForm() {
        saveButton.click();
    }

    public void shouldContainInfoAboutBlankCategoryName() {
        shouldContainText("must not be blank");
    }

    public void shouldContainInfoAboutIncorrectCategoryNameLength() {
        shouldContainText("length must be between 1 and 255");
    }

    public void shouldContainCategory(String categoryName) {
        getDriver().findElement(By.xpath(String.format("//table/thead/tr/th[contains(text(),'%s')]", categoryName)));
    }

    public void shouldNotContainCategory(String categoryName) {
        try {
            shouldContainCategory(categoryName);
        } catch (NoSuchElementException e) {
            // ok
            return;
        }
        fail("Should not find category");
    }

    public void clickEditCategory(String categoryName) {
        getDriver().findElement(By.xpath(String.format("//table/thead/tr/th[contains(text(),'%s')]", categoryName)))
                .findElement(By.xpath("../td[2]/div/a")).click();
    }

    public void clickMoveUpCategory(String categoryName) {
        getDriver().findElement(By.xpath(String.format("//table/thead/tr/th[contains(text(),'%s')]", categoryName)))
                .findElement(By.xpath("../td[1]/div/form[1]/button")).click();
    }

    public void clickMoveDownCategory(String categoryName) {
        getDriver().findElement(By.xpath(String.format("//table/thead/tr/th[contains(text(),'%s')]", categoryName)))
                .findElement(By.xpath("../td[1]/div/form[2]/button")).click();
    }

    public void categoryIsBefore(String firstCategoryName, String secondCategoryName) {
        List<WebElement> webElements = getDriver().findElements(By.xpath("//table/thead/tr/th"));
        List<String> categoriesNames = webElements.stream().map(webElement -> webElement.getText()).collect(Collectors.toList());
        assertThat(categoriesNames.indexOf(firstCategoryName)).isLessThan(categoriesNames.indexOf(secondCategoryName));
    }

    public void clickDeleteCategory(String categoryName) {
        getDriver().findElement(By.xpath(String.format("//table/thead/tr/th[contains(text(),'%s')]", categoryName)))
                .findElement(By.xpath("../td[2]/div/form/button")).click();
    }

    public void clickDeleteButton() {
        deleteButton.click();
    }

    public void clickNewForumButton() {
        newForumButton.click();
    }

    public void typeForumName(String forumName) {
        forumNameField.clear();
        forumNameField.sendKeys(forumName);
    }

    public void shouldContainInfoAboutBlankForumName() {
        shouldContainText("must not be blank");
    }

    public void chooseCategoryForForum(String categoryName) {
        new Select(categorySelect).selectByVisibleText(categoryName);
    }

    public void shouldContainInfoAboutIncorrectForumNameLength() {
        shouldContainText("length must be between 1 and 255");
    }

    public void typeForumDescription(String forumDescription) {
        forumDescriptionTextArea.clear();
        forumDescriptionTextArea.sendKeys(forumDescription);
    }

    public void shouldContainForumInGivenCategory(String forumName, String categoryName) {
        getDriver().findElement(By.xpath(String.format("//table/thead/tr/th[contains(text(),'%s')]", categoryName)))
                .findElement(By.xpath(String.format("../../../tbody/tr/td/a/h4[contains(text(),'%s')]", forumName)));
    }

    public void shouldContainForumDescription(String forumName, String forumDescription) {
        getDriver().findElement(By.xpath(String.format("//table/tbody/tr/td/a/h4[contains(text(),'%s')]", forumName)))
                .findElement(By.xpath(String.format("../../p[contains(text(),'%s')]", forumDescription)));
    }

    public void clickEditForum(String forumName) {
        getDriver().findElement(By.xpath(String.format("//table/tbody/tr/td/a/h4[contains(text(),'%s')]", forumName)))
                .findElement(By.xpath("../../../td[4]/div/a")).click();
    }

    public void setForumCloseStatus(boolean closed) {
        if (closed ^ forumClosedCheckbox.isSelected()) {
            forumClosedCheckbox.click();
        }
    }

    public void shouldContainCloseIconForForum(String forumName) {
        getDriver().findElement(By.xpath(String.format("//table/tbody/tr/td/a/h4[contains(text(),'%s')]", forumName)))
                .findElement(By.xpath("../../../td[1]/img[contains(@src,'/resources/images/closed_message-40.png')]"));
    }

    public void shouldContainOpenIconForForum(String forumName) {
        getDriver().findElement(By.xpath(String.format("//table/tbody/tr/td/a/h4[contains(text(),'%s')]", forumName)))
                .findElement(By.xpath("../../../td[1]/img[contains(@src,'/resources/images/message-40.png')]"));
    }

    public void clickMoveUpForum(String forumName) {
        getDriver().findElement(By.xpath(String.format("//table/tbody/tr/td/a/h4[contains(text(),'%s')]", forumName)))
                .findElement(By.xpath("../../../td[3]/div/form[1]/button")).click();
    }

    public void clickMoveDownForum(String forumName) {
        getDriver().findElement(By.xpath(String.format("//table/tbody/tr/td/a/h4[contains(text(),'%s')]", forumName)))
                .findElement(By.xpath("../../../td[3]/div/form[2]/button")).click();
    }

    public void forumIsBefore(String firstForumName, String secondForumName) {
        List<WebElement> webElements = getDriver().findElements(By.xpath("//table/tbody/tr/td[2]/a/h4"));
        List<String> forumNames = webElements.stream().map(webElement -> webElement.getText()).collect(Collectors.toList());
        assertThat(forumNames.indexOf(firstForumName)).isLessThan(forumNames.indexOf(secondForumName));
    }

    public void clickDeleteForum(String forumName) {
        getDriver().findElement(By.xpath(String.format("//table/tbody/tr/td/a/h4[contains(text(),'%s')]", forumName)))
                .findElement(By.xpath("../../../td[4]/div/form/button")).click();
    }

    public void shouldNotContainForumInGivenCategory(String forumName, String categoryName) {
        try {
            shouldContainForumInGivenCategory(forumName, categoryName);
        } catch (NoSuchElementException e) {
            // ok
            return;
        }
        fail("Should not find forum");
    }

    public void selectOptionForMovingForumsToCategory(String newCategoryName) {
        getDriver().findElement(By.id("removeAndMoveForums")).click();
        new Select(getDriver().findElement(By.id("newCategory"))).selectByVisibleText(newCategoryName);
    }
}
