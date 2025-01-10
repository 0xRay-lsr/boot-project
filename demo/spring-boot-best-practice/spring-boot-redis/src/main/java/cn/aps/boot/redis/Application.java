package cn.aps.boot.redis;

import cn.aps.boot.redis.service.RedisLockService;
import cn.aps.boot.redis.service.RedisOptService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.*;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * @Author : lishirui
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public RedisOptService redisService(RedisTemplate<String, Object> redisTemplate,
                                        ValueOperations<String, Object> valueOperations,
                                        HashOperations<String, String, Object> hashOperations,
                                        SetOperations<String, Object> setOperations,
                                        ListOperations<String, Object> listOperations,
                                        ZSetOperations<String, Object> zSetOperations) {
        return new RedisOptService(redisTemplate, valueOperations,
                hashOperations, listOperations, setOperations, zSetOperations);
    }

    @Bean
    public RedisLockService redisLockService(RedisLockRegistry redisLockRegistry) {
        return new RedisLockService(redisLockRegistry);
    }

}
