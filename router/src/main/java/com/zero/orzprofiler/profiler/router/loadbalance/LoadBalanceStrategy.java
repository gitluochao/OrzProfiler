package com.zero.orzprofiler.profiler.router.loadbalance;

import com.zero.orzprofiler.profiler.router.exception.LoadBalanceException;

/**
 * User: luochao
 * Date: 13-11-26
 * Time: 下午1:46
 */
public interface LoadBalanceStrategy {
    public String choose(String topic,String cliendId) throws LoadBalanceException;
}
