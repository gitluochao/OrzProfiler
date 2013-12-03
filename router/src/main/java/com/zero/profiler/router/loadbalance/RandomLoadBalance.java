package com.zero.profiler.router.loadbalance;

import com.zero.profiler.router.exception.LoadBalanceException;
import java.util.List;
import java.util.Random;

/**
 * User: luochao
 * Date: 13-11-26
 * Time: 下午3:49
 */
public class RandomLoadBalance implements LoadBalanceStrategy {
    RouterMap routerMap = RouterMap.getInstance();
    @Override
    public String choose(String topic, String cliendId) throws LoadBalanceException {
         String broker = routerMap.getExistClientBroker(cliendId);
         if(broker == null){
             List<String> brokers = routerMap.getBrokers(topic);
             int count = brokers.size();
             Random random = new Random();
             if(count > 0){
                 broker = brokers.get(random.nextInt(count));
             }
         }
         routerMap.setExistClientBroker(cliendId,broker);
         return broker;
    }
}
