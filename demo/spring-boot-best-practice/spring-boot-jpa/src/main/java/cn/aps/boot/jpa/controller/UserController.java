package cn.aps.boot.jpa.controller;

import cn.aps.boot.jpa.entity.UserDO;
import cn.aps.boot.jpa.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/user/info/{id}")
    public UserDO getUserInfo(@PathVariable("id") long id){
        UserDO userDO = userRepository.findById(id).orElseGet(null);
        return userDO;
    }

}