package com.ezee.insurence.config;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisCache {
	@Bean
	public RedisCacheConfiguration redisCacheConfiguration() {

		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofMinutes(30))
				.serializeKeysWith(
						RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair
						.fromSerializer(new GenericJackson2JsonRedisSerializer()))
				.disableCachingNullValues();
		return redisCacheConfiguration;
	}

	@Bean
	public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {

		RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisConnectionFactory)
				.cacheDefaults(redisCacheConfiguration())
				.withCacheConfiguration("redisCache", redisCacheConfiguration()).build();

		return redisCacheManager;
	}
}
