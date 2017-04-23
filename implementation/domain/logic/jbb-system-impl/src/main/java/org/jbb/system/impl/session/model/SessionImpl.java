package org.jbb.system.impl.session.model;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.jbb.system.api.model.session.UserSession;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class SessionImpl implements UserSession {

    @NotNull
    private Long id;

    @NotNull
    private LocalDateTime creationTime;

    @NotNull
    private LocalDateTime lastAccessedTime;

    @NotNull
    private LocalDateTime usedTime;

    private Duration inactiveTime;

    @NotEmpty
    @NotNull
    @NotBlank
    private String username;

    @NotEmpty
    @NotNull
    @NotBlank
    private String displayName;

    public SessionImpl(){
        this.id = new Long(0);
        this.creationTime = LocalDateTime.now();
        this.lastAccessedTime = LocalDateTime.now();
        this.usedTime = LocalDateTime.now();
        this.inactiveTime = Duration.of(0, ChronoUnit.MINUTES);
        this.username = "username";
        this.displayName = "displayName";
    }

    @Override
    public Long sessionId() {
        return id;
    }

    @Override
    public LocalDateTime creationTime() {
        return creationTime;
    }

    @Override
    public LocalDateTime lastAccessedTime() {
        return lastAccessedTime;
    }

    @Override
    public Duration usedTime() {
        return null;
    }

    @Override
    public Duration inactiveTime() {
        return inactiveTime;
    }

    @Override
    public String userName() {
        return username;
    }

    @Override
    public String displayUserName() {
        return displayName;
    }
}
