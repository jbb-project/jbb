/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.permissions;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

@DefaultUrl(AcpMemberPermissionsPage.URL)
public class AcpMemberPermissionsPage extends PageObject {

    public static final String URL = "/acp/permissions/global-members";

    @FindBy(id = "memberDisplayedName")
    WebElement memberDisplayedNameField;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Get permissions for member')]")})
    WebElement getPermissionsForMemberButton;

    @FindBys({@FindBy(xpath = "//button[contains(text(),'Save')]")})
    WebElement saveButton;

    @FindBy(id = "customPermissionTable")
    WebElement customPermissionTableCheckbox;

    public void typeMemberDisplayedName(String displayedName) {
        memberDisplayedNameField.clear();
        memberDisplayedNameField.sendKeys(displayedName);
    }

    public void clickGetPermissionsForMember() {
        getPermissionsForMemberButton.click();
    }

    public void clickCustomPermissionTableCheckbox() {
        customPermissionTableCheckbox.click();
    }

    public void clickPermissionCheckbox(String permissionName, PermissionValue permissionValue) {
        getDriver().findElement(By.id(permissionName + "-" + permissionValue.getValue())).click();
    }

    public void clickSaveButton() {
        saveButton.click();
    }

}
