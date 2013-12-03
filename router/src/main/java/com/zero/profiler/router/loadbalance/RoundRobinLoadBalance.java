package com.zero.profiler.router.loadbalance;

import com.zero.profiler.router.exception.LoadBalanceException;

/**
 * User: luochao
 * RoundRobin loadbalance
 * Date: 13-11-26
 * Time: 下午3:45
 */
public class RoundRobinLoadBalance implements LoadBalanceStrategy {
    RouterMap routerMap = RouterMap.getInstance();
    @Override
    public String choose(String topic, String cliendId) throws LoadBalanceException {
        String broker = routerMap.getExistClientBroker(cliendId);
        if(broker == null){
            broker = routerMap.getFollower(topic);
            routerMap.setExistClientBroker(cliendId,broker);
        }
        return broker;
    }
}
