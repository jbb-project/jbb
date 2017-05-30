/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.session;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;

import org.openqa.selenium.WebElement;

@DefaultUrl(AcpSessionManagementPage.URL)
public class AcpSessionManagementPage extends PageObject {

    public static final String URL = "/acp/system/sessions";

    @FindBy(xpath = "//*[@id=\"intervalField\"]")
    WebElement inputForm;

    @FindBy(xpath = "/html/body/div[2]/div[1]/div[2]/div[2]/div/form/button")
    WebElement saveButton;

    @FindBy(xpath = "body > div.container > div:nth-child(1) > div:nth-child(3) > div:nth-child(2) > div > table")
    WebElement userSessionBoardTable;



}
