/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.impl.webhooks.model;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebhookEventDetailValue {

    @Column(name = "value_string")
    private String value;

    @Column(name = "value_class_name")
    private String className;

    public WebhookEventDetailValue(Object value) {
        this.value = value.toString();
        this.className = value.getClass().getName();
    }

    public Object value() {
        Class targetClass;
        try {
            targetClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        PropertyEditor editor = PropertyEditorManager.findEditor(targetClass);
        editor.setAsText(value);
        return editor.getValue();
    }
}
