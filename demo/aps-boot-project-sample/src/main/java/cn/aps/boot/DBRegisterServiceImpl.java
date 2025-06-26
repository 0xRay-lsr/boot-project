package cn.aps.boot;

import java.util.Map;
import java.util.Objects;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/1/17 15:12
 */
public class DBRegisterServiceImpl implements RegisterService {
    @Override
    public void init() {

    }

    @Override
    public void noRegister(Map<String, Objects> keys) {
        //
    }

    @Override
    public RegisterStatus register(Map<String, Objects> keys) {
        //name-age-
        return RegisterStatus.SUCCESS;
    }

    @Override
    public Object idempotent(Map<String, Objects> keys) {
        return null;
    }
}
