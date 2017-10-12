/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.web.commons;

import static org.assertj.core.api.Assertions.assertThat;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@DefaultUrl(NotExistsPage.URL)
public class NotExistsPage extends PageObject {
    public static final String URL = "/some_weird_not_exist_page";

    @FindBy(className = "img-error")
    WebElement imgError404Div;

    public void should_contain_image_with_404_error_info() {
        shouldBeVisible(imgError404Div);
        WebElement img = imgError404Div.findElement(By.tagName("img"));
        assertThat(img.getAttribute("src").endsWith("images/exception404.png")).isTrue();
        shouldBeVisible(img);
    }

}
