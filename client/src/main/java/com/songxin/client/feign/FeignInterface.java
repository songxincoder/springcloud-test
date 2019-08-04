package com.songxin.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("provider")
public interface FeignInterface {
    @RequestMapping(value = "/provider",method = RequestMethod.GET)
    public String provider();
}
