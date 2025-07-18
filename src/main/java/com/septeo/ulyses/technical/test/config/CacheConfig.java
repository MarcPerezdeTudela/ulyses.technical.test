package com.septeo.ulyses.technical.test.config;

import com.septeo.ulyses.technical.test.cache.SimpleCache;
import com.septeo.ulyses.technical.test.entity.Brand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public SimpleCache<Long, Brand> brandCache() {
        return new SimpleCache<>(5 * 60 * 1000); // 5 min TTL
    }
}