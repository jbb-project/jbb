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

import org.apache.commons.lang3.reflect.FieldUtils;
import org.jbb.lib.eventbus.JbbEvent;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Field;
import java.util.Optional;

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

    public WebhookEventDetailValue(Field field, JbbEvent event) {
        try {
            field.setAccessible(true);
            Object object = FieldUtils.readField(field, event);
            this.value = Optional.ofNullable(object).map(Object::toString).orElse(null);
            this.className = field.getType().getName();
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public Object value() {
        if (value == null) {
            return null;
        }
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
