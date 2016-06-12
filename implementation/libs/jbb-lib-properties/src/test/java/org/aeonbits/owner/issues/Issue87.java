/*
 * Copyright (c) 2012-2015, Luigi R. Viggiano
 * All rights reserved.
 *
 * This software is distributable under the BSD license.
 * See the terms of the BSD license in the documentation provided with this software.
 */

package org.aeonbits.owner.issues;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author Luigi R. Viggiano
 */
public class Issue87 {

    public interface MyConfig extends Config {

        @DefaultValue("1, 2, foo, 4")
        Integer[] invalidValueArray();

        @DefaultValue("1, 2, foo, 4")
        MyInteger[] myIntegerInvalidValueArray();

        @DefaultValue("1, 2, foo, 4")
        List<Integer> invalidValueList();

        @DefaultValue("1, 2, foo, 4")
        List<MyInteger> myIntegerInvalidValueList();
    }


    private MyConfig cfg;

    @Before
    public void before() {
        cfg = ConfigFactory.create(MyConfig.class);
    }


    @Test(expected = UnsupportedOperationException.class)
    public void testInvalidValueArray() throws Exception {
        cfg.invalidValueArray();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMyIntegerInvalidValue() throws Exception {
        cfg.myIntegerInvalidValueArray();
    }

    public interface MyConfig extends Config {

        @DefaultValue("1, 2, foo, 4")
        Integer[] invalidValueArray();

        @DefaultValue("1, 2, foo, 4")
        MyInteger[] myIntegerInvalidValueArray();

        @DefaultValue("1, 2, foo, 4")
        List<Integer> invalidValueList();

        @DefaultValue("1, 2, foo, 4")
        List<MyInteger> myIntegerInvalidValueList();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvalidValueList() {
        cfg.invalidValueList();
    }

    public class MyInteger {
        private final Integer value;

        public MyInteger(String value) {
            this.value = new Integer(value);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMyIntegerInvalidValueList() {
        cfg.myIntegerInvalidValueList();
    }


}
