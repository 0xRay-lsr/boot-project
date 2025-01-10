package cn.aps.boot.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : lishirui
 */
@RequiredArgsConstructor
@RestController
public class CacheController {

    private final CacheService cacheService;

    @RequestMapping("/multiply")
    public int multiply(@RequestParam("a") int a,
                        @RequestParam("b") int b) {
        return cacheService.multiply(a, b);
    }
    
}
