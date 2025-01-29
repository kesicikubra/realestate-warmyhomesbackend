//package com.team03.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.*;
//
//import java.text.SimpleDateFormat;
//
//@Configuration
//@EnableCaching
//public class RedisConfig {
//
//    @Value("${spring.data.redis.host}")
//    private String host;
//
//    @Value("${spring.data.redis.port}")
//    private int port;
//    @Value("${spring.data.redis.password}")
//    private String password;
//
//
//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory){
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig ().disableCachingNullValues ()
//                .serializeValuesWith (RedisSerializationContext.SerializationPair.fromSerializer (RedisSerializer.java ()))
//                .serializeKeysWith (RedisSerializationContext.SerializationPair.fromSerializer (new StringRedisSerializer ()))
//                .serializeValuesWith (RedisSerializationContext.SerializationPair.fromSerializer (new GenericJackson2JsonRedisSerializer ()));
//        // .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper ())));
//
//        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory (redisConnectionFactory)
//                .cacheDefaults (redisCacheConfiguration).build ();
//    }
//
//    @Bean
//    public RedisTemplate< String, Object > redisTemplate(RedisConnectionFactory redisConnectionFactory){
//        RedisTemplate< String, Object > template = new RedisTemplate<> ();
//        template.setConnectionFactory (redisConnectionFactory);
//        template.setValueSerializer (new StringRedisSerializer ());
//        template.setKeySerializer (jackson2JsonRedisSerializer ());
//        return template;
//    }
//
//    @Bean
//    public ObjectMapper objectMapper(){
//        ObjectMapper mapper = new ObjectMapper ();
//
//        mapper.disable (SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        mapper.registerModule (new JavaTimeModule ());
//        mapper.setDateFormat (new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"));
//        return mapper;
//    }
//
//    public Jackson2JsonRedisSerializer< Object > jackson2JsonRedisSerializer(){
//        ObjectMapper mapper = new ObjectMapper ();
//        Jackson2JsonRedisSerializer< Object > jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<> (mapper, Object.class);
//
//        mapper.registerModule (new JavaTimeModule ());
//        mapper.disable (SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        mapper.setDateFormat (new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"));
//        return jackson2JsonRedisSerializer;
//    }
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//        redisStandaloneConfiguration.setHostName(host);
//        redisStandaloneConfiguration.setPort(port);
//       // redisStandaloneConfiguration.setPassword(password);
//
//        return new LettuceConnectionFactory (redisStandaloneConfiguration);
//    }
//
//}
