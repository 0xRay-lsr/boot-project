/**
 * @author lishirui
 * @date 2025-10-27
 */
package cn.aps.boot.test.controller;

import cn.aps.boot.test.statemachine.model.Payment;
import cn.aps.boot.test.statemachine.service.PaymentService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Resource
    private PaymentService paymentService;

    @PostMapping("/pay/{id}")
    public Payment pay(@PathVariable("id") Integer id) {
        return paymentService.pay(id);
    }

    @PostMapping("/receive/{id}")
    public Payment receive(@PathVariable("id") Integer id) {
        return paymentService.receive(id);
    }

    @PostMapping("/close/{id}")
    public Payment close(@PathVariable("id") Integer id) {
        return paymentService.close(id);
    }
}
