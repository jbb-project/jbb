package org.jbb.system.web.cache.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HazelcastServerSettingsForm {

    private String members;

    private String groupName;

    private String groupPassword;

    private int serverPort;

}
