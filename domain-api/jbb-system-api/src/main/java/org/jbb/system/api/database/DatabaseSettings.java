package org.jbb.system.api.database;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DatabaseSettings {

    @NotNull
    private CommonDatabaseSettings commonSettings;

    @NotNull
    private DatabaseProviderSettings providerSettings;

}
