package cn.lsr.boot.demo.provider.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Description :第三方服务外调接口
 * @Author : lishirui
 * @Date ：2024/7/30 11:21
 */
@FeignClient(name = "lsr-openfeign-consumer", path = "/consumer")
public interface PartyServices {
    @PostMapping("/party/service01")
    public String getPartyService01(String name);
}
