package cn.aps.boot.features.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 作者：栈长
 * 来源@Author : lishirui
 */
@Service
public class LogService {

    @Autowired
    UserService userService;

}