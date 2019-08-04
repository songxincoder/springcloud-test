package com.songxin.client.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class FeignTest{
        @Autowired
        private FeignInterface feignInterface;

        /**
         * DynamicServerListLoadBalancer for client provider initialized: DynamicServerListLoadBalancer:{NFLoadBalancer:name=provider,current list of Servers=[192.168.0.105:9000],Load balancer stats=Zone stats: {defaultzone=[Zone:defaultzone;	Instance count:1;	Active connections count: 0;	Circuit breaker tripped count: 0;	Active connections per server: 0.0;]
         * },Server stats: [[Server:192.168.0.105:9000;	Zone:defaultZone;	Total Requests:0;	Successive connection failure:0;	Total blackout seconds:0;	Last connection made:Thu Jan 01 08:00:00 CST 1970;	First connection made: Thu Jan 01 08:00:00 CST 1970;	Active Connections:0;	total failure count in last (1000) msecs:0;	average resp time:0.0;	90 percentile resp time:0.0;	95 percentile resp time:0.0;	min resp time:0.0;	max resp time:0.0;	stddev resp time:0.0]
         * ]}ServerList:org.springframework.cloud.netflix.ribbon.eureka.DomainExtractingServerList@37db3845
         */


    @RequestMapping(value = "/clientFeign",method = RequestMethod.GET)
    public String clientFeign(@RequestParam Integer m){

        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result=feignInterface.provider();

        return result;
    }

}
