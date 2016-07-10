/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.form;

import org.jbb.members.api.model.Login;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class LoginFormatter implements Formatter<Login> {

    @Override
    public Login parse(String text, Locale locale) throws ParseException {
        return Login.builder().value(text).build();
    }

    @Override
    public String print(Login object, Locale locale) {
        return object.getValue();
    }
}
