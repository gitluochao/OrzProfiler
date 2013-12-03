package com.zero.profiler.router.loadbalance;

import com.zero.profiler.router.exception.LoadBalanceException;

/**
 * User: luochao
 * Date: 13-11-26
 * Time: 下午3:57
 */
public class RoundRobinStateLessLoadBalance implements LoadBalanceStrategy {
    RouterMap routerMap = RouterMap.getInstance();
    @Override
    public String choose(String topic, String cliendId) throws LoadBalanceException {
        return routerMap.getFollower(topic);
    }
}
