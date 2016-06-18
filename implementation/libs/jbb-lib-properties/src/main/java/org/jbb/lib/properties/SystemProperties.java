package org.jbb.lib.properties;

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config;

public interface SystemProperties extends Config, Accessible {
    @Config.DefaultValue("${user.home}/jbb")
    @Config.Key("JBB_HOME")
    String jbbDirectory();
}
