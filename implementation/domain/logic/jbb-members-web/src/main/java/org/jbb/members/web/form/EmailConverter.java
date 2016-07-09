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


import org.jbb.lib.core.vo.Email;

import java.beans.PropertyEditorSupport;

public class EmailConverter extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        Email u = (Email) getValue();
        return u.getValue();
    }

    @Override
    public void setAsText(String s) {
        setValue(Email.builder().value(s).build());
    }
}
