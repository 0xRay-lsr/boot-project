package cn.aps.boot.demo.provider.service;

import com.alibaba.fastjson.JSON;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description :第三方服务外调接口
 * @Author : lishirui
 * @Date ：2024/7/30 11:21
 */
@FeignClient(name = "lsr-openfeign-consumer", path = "/consumer")
public interface PartyServices {
    @PostMapping("/party/service01")
    public String getPartyService01(String name);
    // api get post delete (param(body，commonheader，bizheader))
}
