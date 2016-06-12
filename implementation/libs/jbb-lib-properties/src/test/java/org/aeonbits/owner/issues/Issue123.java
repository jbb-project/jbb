/*
 * Copyright (c) 2012-2015, Luigi R. Viggiano
 * All rights reserved.
 *
 * This software is distributable under the BSD license.
 * See the terms of the BSD license in the documentation provided with this software.
 */

package org.aeonbits.owner.issues;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.ConfigFactory;
import org.aeonbits.owner.SystemProviderForTest;
import org.aeonbits.owner.UtilTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Luigi R. Viggiano
 */
public class Issue123 {

    private Object save;

    @Before
    public void before() {
        Properties dummySystemProperties = new Properties() {{
            setProperty("user.home", "c:\\user\\home");
        }};

        Map<String, String> dummySystemEnv = new HashMap<String, String>();

        SystemProviderForTest dummySystemForTest = new SystemProviderForTest(dummySystemProperties, dummySystemEnv);

        save = UtilTest.setSystem(dummySystemForTest);
    }

    @After
    public void after() {
        UtilTest.setSystem(save); // restore System env and System props.
    }

    @Test
    public void testHomeWithTildeOnWindows() {
        ConfigFactory.create(BugConfig.class);
        // no exception is expected to be thrown.
    }

    @Sources({"file:~/bug.properties"})
    interface BugConfig extends Config {
    }
}
