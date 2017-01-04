/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties.encrypt;

import org.springframework.stereotype.Component;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

@Component
public class PropertyTypeCasting {

    public Object resolve(String value, Class target) {
        PropertyEditor editor = PropertyEditorManager.findEditor(target);
        editor.setAsText(value);
        return editor.getValue();
    }
}
