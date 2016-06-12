/*
 * Copyright (c) 2012-2015, Luigi R. Viggiano
 * All rights reserved.
 *
 * This software is distributable under the BSD license.
 * See the terms of the BSD license in the documentation provided with this software.
 */

package org.aeonbits.owner.cache;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigCache;
import org.aeonbits.owner.ConfigFactory;
import org.aeonbits.owner.Factory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Luigi R. Viggiano
 */
public class CacheConfigTest {

    @Before
    public void before() {
        ConfigCache.clear();
    }

    @Test
    public void testGetOrCreateFromCache() {
        MyConfig first = ConfigCache.getOrCreate(MyConfig.class);
        MyConfig second = ConfigCache.getOrCreate(MyConfig.class);
        assertSame(first, second);
    }

    @Test
    public void testGetOrCreateWithFactory() {
        Factory factory = ConfigFactory.newInstance();
        Factory spy = spy(factory);
        MyConfig first = ConfigCache.getOrCreate(spy, MyConfig.class);
        MyConfig second = ConfigCache.getOrCreate(spy, MyConfig.class);
        MyConfig third = ConfigCache.getOrCreate(spy, MyConfig.class);
        assertSame(first, second);
        assertSame(second, third);
        verify(spy, times(1)).create(eq(MyConfig.class), Matchers.<Map<?, ?>[]>anyVararg());
    }

    @Test
    public void testGetOrCreateUsingNameKey() {
        MyConfig first = ConfigCache.getOrCreate("MyConfig", MyConfig.class);
        MyConfig second = ConfigCache.getOrCreate("MyConfig", MyConfig.class);
        assertSame(first, second);
    }

    @Test
    public void testRemoveUsingClassAsKey() {
        MyConfig first = ConfigCache.getOrCreate(MyConfig.class);
        Config removed = ConfigCache.remove(MyConfig.class);
        assertNotNull(removed);

        MyConfig second = ConfigCache.getOrCreate(MyConfig.class);
        MyConfig third = ConfigCache.getOrCreate(MyConfig.class);

        assertNotSame(first, second);
        assertSame(second, third);
    }

    @Test
    public void testRemoveUsingNameKey() {
        MyConfig first = ConfigCache.getOrCreate("foo", MyConfig.class);

        Config removed = ConfigCache.remove("foo");
        assertNotNull(removed);

        MyConfig second = ConfigCache.getOrCreate("foo", MyConfig.class);
        MyConfig third = ConfigCache.getOrCreate("foo", MyConfig.class);

        assertNotSame(first, second);
        assertSame(second, third);
    }

    @Test
    public void testRemove() {
        MyConfig first = ConfigCache.getOrCreate("MyConfig", MyConfig.class);
        Config removed = ConfigCache.remove("MyConfig");
        MyConfig second = ConfigCache.getOrCreate("MyConfig", MyConfig.class);
        assertNotSame(first, second);
        assertSame(first, removed);
    }

    @Test
    public void testClear() {
        MyConfig first = ConfigCache.getOrCreate("MyConfig", MyConfig.class);
        ConfigCache.clear();
        MyConfig second = ConfigCache.getOrCreate("MyConfig", MyConfig.class);
        assertNotSame(first, second);
    }

    @Test
    public void testGetWhenInstanceDoesNotExists() {
        MyConfig instance = ConfigCache.get(MyConfig.class);
        assertNull(instance);
    }

    @Test
    public void testGetWhenInstanceExists() {
        MyConfig created = ConfigCache.getOrCreate(MyConfig.class);
        MyConfig got = ConfigCache.get(MyConfig.class);
        assertSame(created, got);
    }

    @Test
    public void testAdd() {
        MyConfig dummy = ConfigFactory.create(MyConfig.class);
        MyConfig previous = ConfigCache.add("foo", dummy);
        MyConfig cached = ConfigCache.getOrCreate("foo", MyConfig.class);

        assertNull(previous);
        assertSame(dummy, cached);
    }

    interface MyConfig extends Config {
    }

}
