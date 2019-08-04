package com.songxin.client.currencystrategy;

import com.songxin.client.filter.UserContext;
import com.songxin.client.filter.UserContextHolder;

import java.util.concurrent.Callable;

public class DelegatingUserContextCallable<V> implements Callable<V> {

    private final Callable<V> delegate;
    private UserContext userContext;

   public DelegatingUserContextCallable(Callable<V> delegate, UserContext userContext){
        this.delegate=delegate;
        this.userContext=userContext;
   }

    @Override
    public V call() throws Exception {
        UserContextHolder.setContext(userContext);
        try{
            return delegate.call();

        }finally {
            this.userContext=null;
        }
    }



    public static <V> Callable<V> create(Callable<V> deletage,UserContext userContext){
       return new DelegatingUserContextCallable<>(deletage,userContext);
    }
}
