package org.jbb.system.impl.cache.logic;

import lombok.RequiredArgsConstructor;
import org.jbb.lib.cache.CacheProperties;
import org.jbb.system.api.cache.CacheSettings;
import org.jbb.system.impl.cache.logic.provider.CacheProvidersService;
import org.jbb.system.impl.cache.logic.provider.HazelcastServerProviderManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheSettingsFactory {

    private final CacheProperties cacheProperties;
    private final CacheProvidersService cacheProvidersService;
    private final HazelcastServerProviderManager hazelcastServerProviderManager;


    public CacheSettings currentCacheSettings() {
        return CacheSettings.builder()
            .applicationCacheEnabled(cacheProperties.applicationCacheEnabled())
            .secondLevelCacheEnabled(cacheProperties.secondLevelCacheEnabled())
            .queryCacheEnabled(cacheProperties.queryCacheEnabled())
            .hazelcastServerSettings(hazelcastServerProviderManager.getCurrentProviderSettings())
            .currentCacheProvider(cacheProvidersService.getCurrentCacheProvider())
            .build();
    }
}
