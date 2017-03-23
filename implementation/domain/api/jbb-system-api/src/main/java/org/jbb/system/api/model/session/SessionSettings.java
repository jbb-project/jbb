package org.jbb.system.api.model.session;


import java.time.LocalDateTime;
import java.util.UUID;

public interface SessionSettings {

    UUID sessionID();

    LocalDateTime creationTime();

    LocalDateTime lastAccessedTime();

    LocalDateTime usedTime();

    LocalDateTime inActiveTime();

    String userName();

    String displayUserName();
}
