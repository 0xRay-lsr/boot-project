/**n * @author lishiruin * @date 2025-10-27n */
package cn.aps.boot.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : lishirui
 */
@RestController
public class UserController {

    @GetMapping(value = "/user/get")
    public Result getUserInfo(@RequestParam("username") String username) {
        Result result = new Result();
        result.setData(username);
        result.setMsg("ok");
        return result;
    }

}
