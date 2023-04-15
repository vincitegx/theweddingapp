package com.slinkdigital.feed.config;

import com.google.common.cache.CacheBuilder;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author TEGA
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(feedsCache()));
        return cacheManager;
    }

    @Bean
    public Cache feedsCache() {
        return new ConcurrentMapCache("feeds",
                CacheBuilder.newBuilder().expireAfterWrite(24, TimeUnit.HOURS).build().asMap(), true);
    }

}
