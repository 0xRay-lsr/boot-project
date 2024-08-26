package cn.lsr.boot.demo.provider.controller;

import cn.lsr.boot.demo.provider.entity.Users;
import cn.lsr.boot.demo.provider.service.PartyServices;
import cn.lsr.boot.demo.provider.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author lishirui
 * @since 2024-07-19 03:08:18
 */
@RestController
@RequestMapping("/provider/users")
public class UsersController {
    Logger logger = LoggerFactory.getLogger(UsersController.class);
    @Autowired
    UsersService usersService;
    @Autowired
    PartyServices partyServices;

    @GetMapping("/test")
    public String createUser() {
        MDC.put("traceId", "test-tranceId");
        Random random = new Random();
        int userId = random.nextInt(9999);
        Users users = new Users().setUserId(userId).setUsername("lishirui"+userId).setFirstName("shirui").setLastName("li").setPassword("rui0306.").setEmail(userId+"103@qq.com");
        logger.info("creater user is : {} ",users.toString());
        usersService.save(users);
        logger.info("start invoker lsr-openfeign-consumer is service the PartyService01...");
        String name = partyServices.getPartyService01("lishirui");
        logger.info("end invoker lsr-openfeign-consumer is service the PartyService01 out count is : {}",name);
        return "success";
    }
}
