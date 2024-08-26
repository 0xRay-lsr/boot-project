package cn.lsr.boot.demo.consumer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2024/7/30 11:20
 */
@RestController
public class ThirdPartyServicesController {
    Logger logger = LoggerFactory.getLogger(ThirdPartyServicesController.class);

    @PostMapping("/consumer/party/service01")
    public String getPartyService01(@RequestBody String name) {
        MDC.put("traceId", "test-tranceId");
        logger.info("process current PartyService01 params is :{}", name);
        return "party service01 name is :" + name;
    }
}
