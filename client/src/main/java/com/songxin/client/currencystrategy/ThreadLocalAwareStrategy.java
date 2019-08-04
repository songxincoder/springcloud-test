package com.songxin.client.currencystrategy;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import com.songxin.client.filter.UserContextHolder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadLocalAwareStrategy extends HystrixConcurrencyStrategy {

    private HystrixConcurrencyStrategy hystrixConcurrencyStrategy;

    public ThreadLocalAwareStrategy(HystrixConcurrencyStrategy hystrixConcurrencyStrategy){
       this. hystrixConcurrencyStrategy=hystrixConcurrencyStrategy;
    }

    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {

        return hystrixConcurrencyStrategy!=null?hystrixConcurrencyStrategy.getBlockingQueue(maxQueueSize): hystrixConcurrencyStrategy!=null?hystrixConcurrencyStrategy.getBlockingQueue(maxQueueSize) :super.getBlockingQueue(maxQueueSize);
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey, HystrixProperty<Integer> corePoolSize, HystrixProperty<Integer> maximumPoolSize, HystrixProperty<Integer> keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        return hystrixConcurrencyStrategy!=null?hystrixConcurrencyStrategy.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue): super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey, HystrixThreadPoolProperties threadPoolProperties) {
        return  hystrixConcurrencyStrategy!=null?hystrixConcurrencyStrategy.getThreadPool(threadPoolKey, threadPoolProperties): super.getThreadPool(threadPoolKey, threadPoolProperties);
    }

    @Override
    public <T> HystrixRequestVariable<T> getRequestVariable(HystrixRequestVariableLifecycle<T> rv) {
        return hystrixConcurrencyStrategy!=null?hystrixConcurrencyStrategy.getRequestVariable(rv): super.getRequestVariable(rv);
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        DelegatingUserContextCallable delegatingUserContextCallable= new DelegatingUserContextCallable<>(callable, UserContextHolder.getContext());
        return hystrixConcurrencyStrategy!=null?hystrixConcurrencyStrategy
                .wrapCallable(delegatingUserContextCallable):super.wrapCallable(delegatingUserContextCallable);
    }
}
