/*
 * Copyright (C) 2017 the original author or authors.
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

@DefaultUrl(RemovingRootLoggerInvalidPage.URL)
public class RemovingRootLoggerInvalidPage extends PageObject {
    public static final String URL = "/acp/general/logging/logger?id=ROOT&act=del";

    public void shouldContainErrorAboutAttemptingToRemoveRootLogger() {
        shouldContainText("You can't remove root logger");
    }

}
