package org.jbb.lib.properties;

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.Mutable;
import org.aeonbits.owner.Reloadable;

/**
 * Marker interface for class which map single property file
 */
public interface ModuleConfig extends Config, Accessible, Reloadable, Mutable {
}
