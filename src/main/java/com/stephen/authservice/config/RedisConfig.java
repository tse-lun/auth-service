package com.stephen.authservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stephen.authservice.dto.CachedUserDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  public RedisTemplate<String, CachedUserDTO> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, CachedUserDTO> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);

    // Use String for keys
    template.setKeySerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());

    // Use GenericJackson2JsonRedisSerializer for values
    GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
    template.setValueSerializer(valueSerializer);
    template.setHashValueSerializer(valueSerializer);

    template.afterPropertiesSet();
    return template;
  }

}