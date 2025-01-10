package cn.aps.boot.test.service;

import org.springframework.stereotype.Service;

/**
 * @Author : lishirui
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public int countAllUsers() {
        return 10;
    }

}
