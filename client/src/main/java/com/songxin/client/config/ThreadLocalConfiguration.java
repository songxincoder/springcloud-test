package com.songxin.client.config;

import com.netflix.hystrix.HystrixMetrics;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import com.songxin.client.currencystrategy.ThreadLocalAwareStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ThreadLocalConfiguration {

    @Autowired(required = false)
    private HystrixConcurrencyStrategy hystrixConcurrencyStrategy;

    @PostConstruct
    public void init() {
        HystrixEventNotifier hystrixEventNotifier= HystrixPlugins.getInstance().getEventNotifier();
        HystrixMetricsPublisher hystrixMetricsPublisher=HystrixPlugins.getInstance().getMetricsPublisher();
        HystrixPropertiesStrategy hystrixPropertiesStrategy=HystrixPlugins.getInstance().getPropertiesStrategy();
        HystrixCommandExecutionHook commandExecutionHook=HystrixPlugins.getInstance().getCommandExecutionHook();
        HystrixPlugins.reset();
        HystrixPlugins.getInstance().registerConcurrencyStrategy(new ThreadLocalAwareStrategy(hystrixConcurrencyStrategy));
        HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);
        HystrixPlugins.getInstance().registerEventNotifier(hystrixEventNotifier);
        HystrixPlugins.getInstance().registerMetricsPublisher(hystrixMetricsPublisher);
        HystrixPlugins.getInstance().registerPropertiesStrategy(hystrixPropertiesStrategy);
    }


}
