package com.songxin.client.discoveryclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class DiscoveryClientTest {

    @Autowired
    private DiscoveryClient discoveryClient; //自动注入discoveryClient

    @RequestMapping(value = "/client",method = RequestMethod.GET)
    public String client(){

        RestTemplate restTemplate=new RestTemplate();

        List<ServiceInstance> provider = discoveryClient.getInstances("provider"); //获取提供者的所有实例列表


        if(provider.size()==0) return "";


        String url=provider.get(0).getUri().toString();//获取地址
        System.out.println(url);

        //RequestEntity<String> requestEntity=new RequestEntity<String>(HttpMethod.GET,url+"/provider");

        ResponseEntity<String> responseEntity=restTemplate.exchange(url+"/provider", HttpMethod.GET,
                HttpEntity.EMPTY,String.class);// 进行调用
        String result=responseEntity.getBody();

        return result;
    }
}
