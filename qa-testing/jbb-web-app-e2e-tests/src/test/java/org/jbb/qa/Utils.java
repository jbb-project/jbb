/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.qa;

import net.serenitybdd.core.Serenity;

public final class Utils {
    public static String currentUrl() {
        return Serenity.getWebdriverManager().getCurrentDriver().getCurrentUrl();
    }
}
