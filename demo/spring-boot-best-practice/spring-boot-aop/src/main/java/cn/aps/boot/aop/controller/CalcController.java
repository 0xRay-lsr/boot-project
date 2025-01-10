package cn.aps.boot.aop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cn.aps.boot.aop.service.*;

/**
 * @Author : lishirui
 */
@RequiredArgsConstructor
@RestController
public class CalcController {

    private final CalcService calcService;

    @GetMapping("/calc/divide")
    public int divide(@RequestParam("param1") int param1,
                      @RequestParam("param2") int param2) {
        return calcService.divide(param1, param2);
    }

}
