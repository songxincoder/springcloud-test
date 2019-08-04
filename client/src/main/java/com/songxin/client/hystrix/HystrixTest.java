package com.songxin.client.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.songxin.client.feign.FeignInterface;
import com.songxin.client.filter.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * hystrix类级别设置
 */
@DefaultProperties(commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "1000")})
@RestController
public class HystrixTest {
    @Autowired
    private FeignInterface feignInterface;


    /**
     *Spring 看到加上HystrixCommand  会为 clientFeign 加上一个代理类，代理将包装该方法，并通过专门的远程调用线程池来管理对该方法的所有调用
     * 默认超出1000 毫秒抛出异常
     */
    /**
     *定制hystrixCommond 超时时间
     * commandProperties={@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value ="200")} 毫秒为单位
     */
    @RequestMapping(value = "/clientHystrix",method = RequestMethod.GET)
    @HystrixCommand(commandProperties={@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value ="200")})
    public String clientHystrix(@RequestParam Integer m){

        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result=feignInterface.provider();

        return result;
    }


    /**
     *fallback 模式，调用超时走 后备策略
     */
    @RequestMapping(value = "/clientHystrixFallback",method = RequestMethod.GET)
    @HystrixCommand(commandProperties={@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value ="200")},
            fallbackMethod = "clientHystrixFallbackLicenseList")
    public String clientHystrixFallback(@RequestParam Integer m){

        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result=feignInterface.provider();

        return result;
    }

    private String clientHystrixFallbackLicenseList(Integer m){
        return "falllback";

    }

    /**
     * hystrix ：断路器模式、 后备模式、舱壁模式
     */


    /**
     * hystrix 舱壁模式：默认情况下所有的请求都会在一个线程池中执行，默认线程池的个数是10；如果请求耗时多，请求增多的情况先线程池的资源会消耗完，导致服务不可用
     *
     * 舱壁模式是 不同的请求服务使用不同的线程池，避免一类请求耗时过多导致其他请求不可用的情况
     */
    @RequestMapping(value = "/clientHystrixThreadPool",method = RequestMethod.GET)
    @HystrixCommand(commandProperties={@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value ="200")},
            fallbackMethod = "clientHystrixThreadPoolLicenseList",
            threadPoolKey = "clientHystrixThreadPool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize",value = "10"),
                    @HystrixProperty(name = "maxQueueSize",value = "10")
            })
    public String clientHystrixThreadPool(){

        /**
         * hystrix 不会将父线程的上线传播到有hystrixcommand 修饰的类中
         * 自定义兵法策略解决这一问题：hystrixConcurrencyStrategy
         */
        System.out.println("clientHystrixThreadPool "+UserContextHolder.getContext().getCorrelationId());


        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result=feignInterface.provider();

        return result;
    }

    private String clientHystrixThreadPoolLicenseList(){
        System.out.println("falllback");
        return "falllback";

    }

    /** hystrix 微调
     * hystrix 断路器执行流程：
     * 1、hystrix 遇到调用失败时会创建一个10s（可配置）的统计窗口，用于统计在这10s内失败次数，如果次数小于最小点用失败次数，hystrix 不会采取行动；
     *
     * 2、如果大于最小失败次数，hystrix 将查看在这10秒内失败次数的百分比，如果真题失败次数的百分比超过了阀值（默认50%），hystrix 将出发断路器，
     * 之后调用几乎全部失败，吐过没有到达阀值且10s 过去，将重新统计
     *
     * 3、到达阀值了，hystrix 会开启一个窗口，每隔 5s（可配置） 会让一个请求通过，去请求服务，用于检测服务是否恢复正常，调用成功，
     * 重值断路器 让请求通过，失败，下一次 5s 重新尝试；
     *
     *
     *threadpoolProperties 用于设置 hystrix 命令中使用的底层线程池行为
     * commandpoolproperties 用于与hystrix 命令关联的断路器行为
     */

    /**
     * hystrix 隔离策略： thread 、semaphore 信号量（轻量级，用于高并发 nio 模式，netty，不开启新的线程，会中断父线程）
     */
}

