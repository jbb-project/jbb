package org.jbb.members.api.model;


import lombok.experimental.Tolerate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class IPAddress {

    @NotNull
    @Pattern(regexp =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$")
    private String ipAddress;

    @Tolerate
    IPAddress() {
        // for JPA
    }

}
