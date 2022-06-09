package org.gentar.biology.project.caching;


import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CachingApplicationConfiguration {

    @Bean
    public CacheManager cacheManager() {

        return new ConcurrentMapCacheManager() {

            @Override
            protected Cache createConcurrentMapCache(String name) {
                return new ConcurrentMapCollectionHandlingDecoratedCache(
                    super.createConcurrentMapCache(name));
            }
        };
    }

}