package cn.aps.boot.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author : lishirui
 */
@Slf4j
@Controller
public class ErrorPageController implements ErrorController {

    @GetMapping(value = "/error")
    public String handleError() {
        return "redirect:/";
    }

}