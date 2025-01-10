package cn.aps.boot.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 登录接口
 * @Author : lishirui
 */
@Slf4j
@Controller
public class LoginController {


    @GetMapping("/logout")
    public void logout() {
        log.info("log out...");
    }

}
