package com.slinkdigital.user.config;

import com.slinkdigital.user.exception.UserException;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * @author TEGA
 */
@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {
    
    public static final String USERDTO = "userdto";
    
    @Bean
    public CacheManager cacheManager(){
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager(USERDTO);
        return cacheManager;
    }
    
    @Scheduled(fixedRate = 3599999)
    public void evictAllCaches(){
        try{
            cacheManager().getCache(USERDTO).clear();
        }catch(NullPointerException ex){
            throw new UserException(ex.getMessage());
        }
    }    
}
