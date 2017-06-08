/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity;

import net.serenitybdd.core.Serenity;

import org.openqa.selenium.Cookie;

import java.util.Set;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utils {

    public static String current_url() {
        return Serenity.getWebdriverManager().getCurrentDriver().getCurrentUrl();
    }

    public static Set<Cookie> get_current_cookies() {
        return Serenity.getWebdriverManager().getCurrentDriver().manage().getCookies();
    }

    public static void delete_all_cookies() {
        Serenity.getWebdriverManager().getCurrentDriver().manage().deleteAllCookies();
    }

    public static void set_cookies(Set<Cookie> cookies) {
        cookies.forEach(cookie -> Serenity.getWebdriverManager().getCurrentDriver().manage().addCookie(cookie));
    }

}
