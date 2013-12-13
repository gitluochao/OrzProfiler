package com.zero.orzprofiler.profiler.router.loadbalance;

import com.zero.orzprofiler.profiler.router.exception.LoadBalanceException;

/**
 * User: luochao
 * Date: 13-11-26
 * Time: 下午3:43
 */
public class LoadBalanceContext {
    private LoadBalanceStrategy strategy;

    public LoadBalanceContext(LoadBalanceStrategy strategy) {
        this.strategy = strategy;
    }

    public String choose(String topic,String cliendId) throws LoadBalanceException{
       return  strategy.choose(topic,cliendId);
    }

}
