/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties.encrypt;


import static org.apache.commons.lang3.StringUtils.substringBetween;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EncryptionPlaceholderUtils {

    public static boolean isInDecPlaceholder(String value) {
        return extractFromDecPlaceholder(value) != null;
    }

    public static String extractFromDecPlaceholder(String value) {
        String result = substringBetween(value, "DEC(", ")");
        if (result == null) {
            result = substringBetween(value, "dec(", ")");
        }
        return result;
    }

    public static boolean isInEncPlaceholder(String value) {
        return extractFromEncPlaceholder(value) != null;
    }

    public static String extractFromEncPlaceholder(String value) {
        String result = substringBetween(value, "ENC(", ")");
        if (result == null) {
            result = substringBetween(value, "enc(", ")");
        }
        return result;
    }

    public static String surroundWithDecPlaceholder(String value) {
        return value == null ? null : "DEC(" + value + ")";
    }

    public static String surroundWithEncPlaceholder(String value) {
        return value == null ? null : "ENC(" + value + ")";
    }
}
