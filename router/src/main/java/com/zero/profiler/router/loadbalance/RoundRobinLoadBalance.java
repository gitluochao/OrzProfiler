package com.zero.profiler.router.loadbalance;

import com.zero.profiler.router.exception.LoadBalanceException;

/**
 * User: luochao
 * RoundRobin loadbalance
 * Date: 13-11-26
 * Time: 下午3:45
 */
public class RoundRobinLoadBalance implements LoadBalanceStrategy {
    @Override
    public String choose(String topic, String cliendId) throws LoadBalanceException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
