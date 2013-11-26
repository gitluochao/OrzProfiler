package com.zero.profiler.router.loadbalance;

import com.zero.profiler.router.exception.LoadBalanceException;

/**
 * User: luochao
 * Date: 13-11-26
 * Time: 下午3:49
 */
public class RandomLoadBalance implements LoadBalanceStrategy {
    @Override
    public String choose(String topic, String cliendId) throws LoadBalanceException {
        return null;
    }
}
