package cn.aps.boot.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Author : lishirui
 */
@Slf4j
@Service
public class CacheService {

    @Cacheable("calc")
    public int multiply(int a, int b) {
        int c = a * b;
        log.info("{} * {} = {}", a, b, c);
        return c;
    }

}