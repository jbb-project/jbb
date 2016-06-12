/*
 * Copyright (c) 2012-2015, Luigi R. Viggiano
 * All rights reserved.
 *
 * This software is distributable under the BSD license.
 * See the terms of the BSD license in the documentation provided with this software.
 */

package org.aeonbits.owner;

import org.aeonbits.owner.Config.DisableFeature;
import org.junit.Test;

import static org.aeonbits.owner.Config.DisableableFeature.PARAMETER_FORMATTING;
import static org.aeonbits.owner.Config.DisableableFeature.VARIABLE_EXPANSION;
import static org.junit.Assert.assertEquals;

/**
 * @author Luigi R. Viggiano
 */
public class DisableFeatureTest {
    @Test
    public void shouldNotExpandWorldWhenDisabledOnMethodLevel() {
        ConfigWithSubstitutionDisabledOnMethod cfg = ConfigFactory.create(ConfigWithSubstitutionDisabledOnMethod.class);
        assertEquals("Hello ${world}.", cfg.sayHelloDisabled());
        assertEquals("Hello Earth.", cfg.sayHelloEnabled());
    }

    @Test
    public void shouldNotExpandWorldWhenDisabledOnClassLevel() {
        ConfigWithSubstitutionDisabledOnClass cfg = ConfigFactory.create(ConfigWithSubstitutionDisabledOnClass.class);
        assertEquals("Hello ${world}.", cfg.sayHelloDisabled());
        assertEquals("Hello ${world}.", cfg.sayHelloEnabled());
    }

    @Test
    public void shouldNotFormatOnMethodLevel() {
        ConfigWithDisabledFormattingOnMethod cfg = ConfigFactory.create(ConfigWithDisabledFormattingOnMethod.class);
        assertEquals("Hello %s.", cfg.helloDisabled("world"));
        assertEquals("Hello world.", cfg.helloEnabled("world"));
    }

    @Test
    public void shouldNotFormatOnClassLevel() {
        ConfigWithDisabledFormattingOnClass cfg = ConfigFactory.create(ConfigWithDisabledFormattingOnClass.class);
        assertEquals("Hello %s.", cfg.helloDisabled("world"));
        assertEquals("Hello %s.", cfg.helloEnabled("world"));
    }

    @Test
    public void shouldNotFormatAndExpandOnMethod() {
        ConfigWithDisabledFormattingAndExpansionOnMethod cfg =
                ConfigFactory.create(ConfigWithDisabledFormattingAndExpansionOnMethod.class);
        assertEquals("Hello Luigi, welcome on Earth!", cfg.helloEnabled("Luigi"));
        assertEquals("Hello %s, welcome on ${planet}!", cfg.helloDisabled("Luigi"));
    }

    @Test
    public void shouldNotFormatAndExpandOnClass() {
        ConfigWithDisabledFormattingAndExpansionOnClass cfg =
                ConfigFactory.create(ConfigWithDisabledFormattingAndExpansionOnClass.class);
        assertEquals("Hello %s, welcome on ${planet}!", cfg.helloEnabled("Luigi"));
        assertEquals("Hello %s, welcome on ${planet}!", cfg.helloDisabled("Luigi"));
    }

    public interface ConfigWithSubstitutionDisabledOnMethod extends Config {
        @DefaultValue("Earth")
        String world();

        @DisableFeature(VARIABLE_EXPANSION)
        @DefaultValue("Hello ${world}.")
        String sayHelloDisabled();

        @DefaultValue("Hello ${world}.")
        String sayHelloEnabled();
    }

    @DisableFeature(VARIABLE_EXPANSION)
    public interface ConfigWithSubstitutionDisabledOnClass extends Config {
        @DefaultValue("Earth")
        String world();

        @DefaultValue("Hello ${world}.")
        String sayHelloDisabled();

        @DefaultValue("Hello ${world}.")
        String sayHelloEnabled();
    }

    public interface ConfigWithDisabledFormattingOnMethod extends Config {
        @DisableFeature(PARAMETER_FORMATTING)
        @DefaultValue("Hello %s.")
        String helloDisabled(String name);

        @DefaultValue("Hello %s.")
        String helloEnabled(String name);
    }

    @DisableFeature(PARAMETER_FORMATTING)
    public interface ConfigWithDisabledFormattingOnClass extends Config {
        @DefaultValue("Hello %s.")
        String helloDisabled(String name);

        @DefaultValue("Hello %s.")
        String helloEnabled(String name);
    }

    public interface ConfigWithDisabledFormattingAndExpansionOnMethod extends Config {
        @DefaultValue("Earth")
        String planet();

        @DisableFeature({VARIABLE_EXPANSION, PARAMETER_FORMATTING})
        @DefaultValue("Hello %s, welcome on ${planet}!")
        String helloDisabled(String name);

        @DefaultValue("Hello %s, welcome on ${planet}!")
        String helloEnabled(String name);
    }

    @DisableFeature({VARIABLE_EXPANSION, PARAMETER_FORMATTING})
    public interface ConfigWithDisabledFormattingAndExpansionOnClass extends Config {
        @DefaultValue("Earth")
        String planet();

        @DisableFeature({VARIABLE_EXPANSION, PARAMETER_FORMATTING})
        @DefaultValue("Hello %s, welcome on ${planet}!")
        String helloDisabled(String name);

        @DefaultValue("Hello %s, welcome on ${planet}!")
        String helloEnabled(String name);
    }
}
