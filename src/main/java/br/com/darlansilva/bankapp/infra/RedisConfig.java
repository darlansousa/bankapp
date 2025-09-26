package br.com.darlansilva.bankapp.infra;

import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.darlansilva.bankapp.entrypoint.api.dto.output.AccountBalanceOutputDto;
import br.com.darlansilva.bankapp.entrypoint.api.dto.output.AccountOutputDto;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Bean
    CacheManager cacheManager(RedisConnectionFactory cf, ObjectMapper objectMapper) {
        final var valueSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        final var config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30))
                .serializeKeysWith(fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(fromSerializer(valueSerializer));
        return RedisCacheManager.builder(cf).cacheDefaults(config)
                .withInitialCacheConfigurations(configPerCache(objectMapper, config))
                .build();
    }

    private static Map<String, RedisCacheConfiguration> configPerCache(
            ObjectMapper objectMapper,
            RedisCacheConfiguration config) {

        var accounts = jacksonSerList(objectMapper, AccountOutputDto.class);
        var balance = jacksonSer(objectMapper, AccountBalanceOutputDto.class);
        Map<String, RedisCacheConfiguration> perCache = new HashMap<>();
        perCache.put("accounts", config.serializeValuesWith(fromSerializer(accounts)));
        perCache.put("balance", config.serializeValuesWith(fromSerializer(balance)));
        return perCache;
    }

    private static <T> Jackson2JsonRedisSerializer<T> jacksonSer(ObjectMapper objectMapper, Class<T> type) {
        return new Jackson2JsonRedisSerializer<>(objectMapper, type);
    }

    private static <T> Jackson2JsonRedisSerializer<List<T>> jacksonSerList(ObjectMapper objectMapper,
                                                                           Class<T> clazz) {
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
        return new Jackson2JsonRedisSerializer<>(objectMapper, type);
    }

}
