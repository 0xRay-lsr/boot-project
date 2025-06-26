package cn.aps.boot.test.invoke.api;

import okhttp3.internal.http.HttpMethod;
import org.springframework.http.HttpEntity;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/2/11 16:45
 */
public interface HttpClient {
    <T> T get(String url, Class<T> responseType);
    <T> T post(String url, Object request, Class<T> responseType);
    <T> T exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType);
}
