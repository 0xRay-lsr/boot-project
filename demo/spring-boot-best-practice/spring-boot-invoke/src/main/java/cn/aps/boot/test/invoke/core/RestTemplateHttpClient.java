package cn.aps.boot.test.invoke.core;

import cn.aps.boot.test.invoke.api.HttpClient;
import okhttp3.internal.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/2/11 19:08
 */
public class RestTemplateHttpClient implements HttpClient {

    private final RestTemplate restTemplate;

    public RestTemplateHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public <T> T get(String url, Class<T> responseType) {
        return null;
    }

    @Override
    public <T> T post(String url, Object request, Class<T> responseType) {
        return null;
    }

    @Override
    public <T> T exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType) {
        return null;
    }
}
