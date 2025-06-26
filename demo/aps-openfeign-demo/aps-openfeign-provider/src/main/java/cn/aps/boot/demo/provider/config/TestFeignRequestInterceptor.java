package cn.aps.boot.demo.provider.config;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/2/18 09:40
 */
//@Component
public class TestFeignRequestInterceptor implements RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TestFeignRequestInterceptor.class);

    @Override
    public void apply(RequestTemplate template) {
        // 打印请求 URL 和 Headers
        logger.info("Feign request URL: {}", template.url());
        logger.info("Feign request Headers: {}", template.headers());

        // 如果需要打印请求体，可以获取 request body（需要额外处理）
    }
}