package org.jbb.system.impl.cache.logic.provider;

import org.jbb.system.api.cache.CacheProvider;
import org.jbb.system.api.cache.CacheProviderSettings;
import org.jbb.system.api.cache.CacheSettings;

public interface CacheProviderManager<T extends CacheProviderSettings> {

    CacheProvider getProviderName();

    T getCurrentProviderSettings();

    void setProviderSettings(CacheSettings newCacheSettings);

    default void setAsCurrentProvider(CacheSettings newCacheSettings) {
        newCacheSettings.setCurrentCacheProvider(getProviderName());
    }

}
