/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties;

import org.springframework.stereotype.Component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import lombok.extern.slf4j.Slf4j;

import static org.jbb.lib.properties.encrypt.EncryptionPlaceholderUtils.isInDecPlaceholder;
import static org.jbb.lib.properties.encrypt.EncryptionPlaceholderUtils.isInEncPlaceholder;

@Slf4j
@Component
class LoggingPropertyChangeListener implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (isPublic(evt.getOldValue()) && isReadyToBeEncrypted(evt.getNewValue())) {
            log.info("Property '{}' in {} will be encrypted",
                    evt.getPropertyName(), evt.getPropagationId());
        } else if (isReadyToBeEncrypted(evt.getOldValue()) && isEncrypted(evt.getNewValue())) {
            log.info("Property '{}' in {} had been encrypted to '{}'",
                    evt.getPropertyName(), evt.getPropagationId(), evt.getNewValue());
        } else {
            log.info("Property '{}' in {} changed from '{}' to '{}'",
                    evt.getPropertyName(), evt.getPropagationId(), evt.getOldValue(), evt.getNewValue());
        }
    }

    private boolean isPublic(Object value) {
        return !isReadyToBeEncrypted(value) && !isEncrypted(value);
    }

    private boolean isReadyToBeEncrypted(Object value) {
        return isInEncPlaceholder(value.toString());
    }

    private boolean isEncrypted(Object value) {
        return isInDecPlaceholder(value.toString());
    }

}
