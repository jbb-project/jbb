package org.jbb.lib.db.provider;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.db.DbProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseProviderService {

    private static final Map<String, Class<? extends DatabaseProvider>> PROVIDERS =
        ImmutableMap.<String, Class<? extends DatabaseProvider>>builder()
            .put(H2InMemoryProvider.PROVIDER_VALUE, H2InMemoryProvider.class)
            .put(H2ManagedServerProvider.PROVIDER_VALUE, H2ManagedServerProvider.class)
            .build();

    private final DbProperties dbProperties;
    private final ApplicationContext appContext;

    public DatabaseProvider getCurrentProvider() {
        String providerName = dbProperties.currentProvider();
        return getProviderForName(providerName);
    }

    private DatabaseProvider getProviderForName(String providerName) {
        Class managerClass = PROVIDERS.get(providerName.trim().toLowerCase());

        if (managerClass != null) {
            return (DatabaseProvider) appContext.getBean(managerClass);
        }

        throw new IllegalStateException(
            String.format("No database provider with name: %s", providerName));

    }

}
