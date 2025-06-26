package cn.aps.boot;

import java.util.Map;
import java.util.Objects;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/1/17 15:04
 */
public interface RegisterService {
    void init();
    void noRegister(Map<String, Objects> keys);
    RegisterStatus register(Map<String, Objects> keys);
    Object idempotent(Map<String, Objects> keys);
}
