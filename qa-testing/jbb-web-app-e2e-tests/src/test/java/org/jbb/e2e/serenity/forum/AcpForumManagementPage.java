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

import net.serenitybdd.core.annotations.findby.By;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@DefaultUrl(AcpForumManagementPage.URL)
public class AcpForumManagementPage extends PageObject {
    public static final String URL = "/acp/general/forums";

    @FindBys({@FindBy(linkText = "New forum category")})
    WebElement newForumCategoryButton;

    @FindBy(id = "categoryName")
    WebElement categoryNameField;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Save')]")})
    WebElement saveButton;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Delete')]")})
    WebElement deleteButton;

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

    public void shouldContainInfoAboutEmptyCategoryName() {
        shouldContainText("may not be empty");
    }

    public void shouldContainInfoAboutIncorrectCategoryNameLength() {
        shouldContainText("length must be between 1 and 255");
    }

    public void shouldContainCategory(String categoryName) {
        getDriver().findElement(By.xpath(String.format("//table/thead/tr/th[contains(text(),'%s')]", categoryName)));
    }

    public void shouldNotContainCategory(String categoryName) {
        try {
            getDriver().findElement(By.xpath(String.format("//table/thead/tr/th[contains(text(),'%s')]", categoryName)));
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
        Point firstCategoryLocation = getDriver().findElement(By.xpath(String.format("//table/thead/tr/th[contains(text(),'%s')]", firstCategoryName))).getLocation();
        Point secondCategoryLocation = getDriver().findElement(By.xpath(String.format("//table/thead/tr/th[contains(text(),'%s')]", secondCategoryName))).getLocation();

        assertThat(firstCategoryLocation.getY()).isLessThan(secondCategoryLocation.getY());
    }

    public void clickDeleteCategory(String categoryName) {
        getDriver().findElement(By.xpath(String.format("//table/thead/tr/th[contains(text(),'%s')]", categoryName)))
                .findElement(By.xpath("../td[2]/div/form/button")).click();
    }

    public void clickDeleteButton() {
        deleteButton.click();
    }

}