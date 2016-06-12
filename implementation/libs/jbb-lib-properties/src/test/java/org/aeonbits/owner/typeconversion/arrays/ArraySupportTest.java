/*
 * Copyright (c) 2012-2015, Luigi R. Viggiano
 * All rights reserved.
 *
 * This software is distributable under the BSD license.
 * See the terms of the BSD license in the documentation provided with this software.
 */

package org.aeonbits.owner.typeconversion.arrays;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Luigi R. Viggiano
 * @author Dmytro Chyzhykov
 */
public class ArraySupportTest {

    private ArrayConfig cfg;

    @Before
    public void before() {
        cfg = ConfigFactory.create(ArrayConfig.class);
    }

    @Test
    public void itShouldReadStringArray() throws Exception {
        assertThat(cfg.fruit(), is(new String[]{"apple", "pear", "orange"}));
    }

    @Test
    public void itShouldReturnNullForMissedStringArray() throws Exception {
        assertThat(cfg.missedProperty(), is(nullValue()));
    }

    @Test
    public void itShouldReturnEmptyStringArray() throws Exception {
        assertThat(cfg.emptyProperty(), is(new String[]{}));
    }

    @Test
    public void itShouldReturnIntegerArray() throws Exception {
        assertThat(cfg.integers(), is(new Integer[]{1, 2, 3}));
    }

    @Test
    public void itShouldReturnEmptyIntegerArray() throws Exception {
        assertThat(cfg.emptyIntegers(), is(new Integer[]{}));
    }

    @Test
    public void itShouldReturnIntArray() throws Exception {
        assertThat(cfg.primitiveIntArray(), is(new int[]{1, 2, 3}));
    }

    @Test
    public void itShouldReturnEmptyIntArray() throws Exception {
        assertThat(cfg.primitiveEmptyIntegers(), is(new int[]{}));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedArrayType() throws Exception {
        cfg.unsupported();
    }

    /**
     * emptyProperty= unsupported=dummy value: this is unsupported
     */
    public interface ArrayConfig extends Config {
        @DefaultValue("apple, pear, orange")
        String[] fruit();

        @DefaultValue("")
        String[] emptyProperty();

        String[] missedProperty();

        @DefaultValue("1, 2, 3")
        Integer[] integers();

        @DefaultValue("${emptyProperty}")
        Integer[] emptyIntegers();

        @DefaultValue("${integers}")
        int[] primitiveIntArray();

        @DefaultValue("${emptyIntegers}")
        int[] primitiveEmptyIntegers();

        @DefaultValue("dummy value: this is unsupported")
        UnsupportedType[] unsupported();

        class UnsupportedType {
        }
    }
}
