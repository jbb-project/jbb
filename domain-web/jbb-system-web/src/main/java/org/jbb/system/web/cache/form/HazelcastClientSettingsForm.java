package org.jbb.system.web.cache.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HazelcastClientSettingsForm {

    private String members;

    private String groupName;

    private String groupPassword;

    private int connectionTimeout;

    private int connectionAttemptPeriod;

    private int connectionAttemptLimit;
}
