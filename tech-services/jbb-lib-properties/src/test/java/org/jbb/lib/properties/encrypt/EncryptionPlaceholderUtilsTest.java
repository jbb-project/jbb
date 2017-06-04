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

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jbb.lib.properties.encrypt.EncryptionPlaceholderUtils.isInDecPlaceholder;
import static org.jbb.lib.properties.encrypt.EncryptionPlaceholderUtils.isInEncPlaceholder;
import static org.jbb.lib.properties.encrypt.EncryptionPlaceholderUtils.surroundWithDecPlaceholder;
import static org.jbb.lib.properties.encrypt.EncryptionPlaceholderUtils.surroundWithEncPlaceholder;

public class EncryptionPlaceholderUtilsTest {

    @Test
    public void null_isNotInDecPlaceholder() throws Exception {
        // given
        String nullString = null;
        // when
        boolean result = isInDecPlaceholder(nullString);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void emptyString_isNotInDecPlaceholder() throws Exception {
        // given
        String emptyString = StringUtils.EMPTY;
        // when
        boolean result = isInDecPlaceholder(emptyString);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void decPlaceholder_isInDecPlaceholder() throws Exception {
        // given
        String lowercase = "dec()";
        String uppercase = "DEC()";
        String mix = "Dec()";
        String encString = "enc()";

        // when
        boolean result = isInDecPlaceholder(lowercase);
        boolean result2 = isInDecPlaceholder(uppercase);
        boolean result3 = isInDecPlaceholder(mix);
        boolean result4 = isInDecPlaceholder(encString);

        // then
        assertThat(result).isTrue();
        assertThat(result2).isTrue();
        assertThat(result3).isFalse();
        assertThat(result4).isFalse();
    }

    @Test
    public void contentSurroundedByDecPlaceholder_isInDecPlaceholder() throws Exception {
        // given
        String lowercase = "dec(test)";
        String uppercase = "DEC(test)";
        String mix = "Dec(test)";
        String encString = "enc(test)";

        // when
        boolean result = isInDecPlaceholder(lowercase);
        boolean result2 = isInDecPlaceholder(uppercase);
        boolean result3 = isInDecPlaceholder(mix);
        boolean result4 = isInDecPlaceholder(encString);

        // then
        assertThat(result).isTrue();
        assertThat(result2).isTrue();
        assertThat(result3).isFalse();
        assertThat(result4).isFalse();
    }

    @Test
    public void null_isNotInEncPlaceholder() throws Exception {
        // given
        String nullString = null;
        // when
        boolean result = isInEncPlaceholder(nullString);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void emptyString_isNotInEncPlaceholder() throws Exception {
        // given
        String emptyString = StringUtils.EMPTY;
        // when
        boolean result = isInEncPlaceholder(emptyString);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void encPlaceholder_isInEncPlaceholder() throws Exception {
        // given
        String lowercase = "enc()";
        String uppercase = "ENC()";
        String mix = "Enc()";
        String encString = "dec()";

        // when
        boolean result = isInEncPlaceholder(lowercase);
        boolean result2 = isInEncPlaceholder(uppercase);
        boolean result3 = isInEncPlaceholder(mix);
        boolean result4 = isInEncPlaceholder(encString);

        // then
        assertThat(result).isTrue();
        assertThat(result2).isTrue();
        assertThat(result3).isFalse();
        assertThat(result4).isFalse();
    }

    @Test
    public void contentSurroundedByEncPlaceholder_isInEncPlaceholder() throws Exception {
        // given
        String lowercase = "enc(test)";
        String uppercase = "ENC(test)";
        String mix = "Enc(test)";
        String encString = "dec(test)";

        // when
        boolean result = isInEncPlaceholder(lowercase);
        boolean result2 = isInEncPlaceholder(uppercase);
        boolean result3 = isInEncPlaceholder(mix);
        boolean result4 = isInEncPlaceholder(encString);

        // then
        assertThat(result).isTrue();
        assertThat(result2).isTrue();
        assertThat(result3).isFalse();
        assertThat(result4).isFalse();
    }

    @Test
    public void nullSurroundedByEncPlaceholder_isStillNull() throws Exception {
        // given
        String nullString = null;

        // when
        String result = surroundWithEncPlaceholder(nullString);

        // then
        assertThat(result).isNull();
    }

    @Test
    public void nullSurroundedByDecPlaceholder_isStillNull() throws Exception {
        // given
        String nullString = null;

        // when
        String result = surroundWithDecPlaceholder(nullString);

        // then
        assertThat(result).isNull();
    }

    @Test
    public void textShouldBeSurroundedByEncPlaceholder() throws Exception {
        // given
        String text = "board";

        // when
        String result = surroundWithEncPlaceholder(text);

        // then
        assertThat(result).isEqualTo("ENC(board)");
    }

    @Test
    public void textShouldBeSurroundedByDecPlaceholder() throws Exception {
        // given
        String text = "board";

        // when
        String result = surroundWithDecPlaceholder(text);

        // then
        assertThat(result).isEqualTo("DEC(board)");
    }

}