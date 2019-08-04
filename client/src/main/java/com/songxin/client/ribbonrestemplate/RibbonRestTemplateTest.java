package com.songxin.client.ribbonrestemplate;

import com.songxin.client.filter.UserContextInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@RestController
public class RibbonRestTemplateTest {

    @LoadBalanced // 创建一个支持 ribbon 的restTemplate类
    @Bean
    public RestTemplate getRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if(interceptors==null){
            restTemplate.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
        }else{
            interceptors.add(new UserContextInterceptor());
            restTemplate.setInterceptors(interceptors);
        }
        return restTemplate;
    }

    @Autowired
    private RestTemplate restTemplate;

    /**
     *
     * 这个实例中不需要用到 EnableDiscoveryClient、EnableEurekaClient 注解，因为是restemplate 中支持了ribbon，而上一个中用到discoveryclient从ribbon 中获取实例url
     */

    @RequestMapping(value = "/clientBalanced",method = RequestMethod.GET)
    public String clientBalanced(){

        ResponseEntity<String> responseEntity=restTemplate.exchange("http://provider/provider", HttpMethod.GET,
                HttpEntity.EMPTY,String.class);// 根据服务名称进行调用

        /**
         * 会出现：
         * DynamicServerListLoadBalancer for client provider initialized: DynamicServerListLoadBalancer:{NFLoadBalancer:name=provider,current list of Servers=[192.168.0.105:9000],Load balancer stats=Zone stats: {defaultzone=[Zone:defaultzone;	Instance count:1;	Active connections count: 0;	Circuit breaker tripped count: 0;	Active connections per server: 0.0;]
         * },Server stats: [[Server:192.168.0.105:9000;	Zone:defaultZone;	Total Requests:0;	Successive connection failure:0;	Total blackout seconds:0;	Last connection made:Thu Jan 01 08:00:00 CST 1970;	First connection made: Thu Jan 01 08:00:00 CST 1970;	Active Connections:0;	total failure count in last (1000) msecs:0;	average resp time:0.0;	90 percentile resp time:0.0;	95 percentile resp time:0.0;	min resp time:0.0;	max resp time:0.0;	stddev resp time:0.0]
         * ]}ServerList:org.springframework.cloud.netflix.ribbon.eureka.DomainExtractingServerList@72d99a16
         */

        String result=responseEntity.getBody();

        return result;
    }
}
