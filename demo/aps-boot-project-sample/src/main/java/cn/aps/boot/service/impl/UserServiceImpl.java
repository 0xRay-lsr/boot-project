package cn.aps.boot.service.impl;

import cn.aps.boot.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public String sayHello() {
        return "hello world";
    }
}
