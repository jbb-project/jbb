/*
 * Copyright (c) 2012-2015, Luigi R. Viggiano
 * All rights reserved.
 *
 * This software is distributable under the BSD license.
 * See the terms of the BSD license in the documentation provided with this software.
 */

package org.aeonbits.owner.util;

import java.lang.reflect.Method;

/**
 * @author Luigi R. Viggiano
 */
public final class Reflection {

    private static final Java8Support JAVA_8_SUPPORT = getJava8Support();

    // Suppresses default LOOKUP_CONSTRUCTOR, ensuring non-instantiability.
    private Reflection() {
    }

    public static boolean isClassAvailable(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static Java8Support getJava8Support() {
        try {
            return (Java8Support) Class.forName("org.aeonbits.owner.util.Java8SupportImpl").newInstance();
        } catch (Exception e) {
            return java8NotSupported();
        }
    }

    private static Java8Support java8NotSupported() {
        return new Java8Support() {
            public boolean isDefault(Method method) {
                return false;
            }

            public Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        };
    }

    public static boolean isDefault(Method method) {
        return JAVA_8_SUPPORT.isDefault(method);
    }

    public static Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable {
        return JAVA_8_SUPPORT.invokeDefaultMethod(proxy, method, args);
    }

    interface Java8Support {
        boolean isDefault(Method method);

        Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable;
    }

}
