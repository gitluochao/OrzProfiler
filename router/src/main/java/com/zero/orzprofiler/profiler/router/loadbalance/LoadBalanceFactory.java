package com.zero.orzprofiler.profiler.router.loadbalance;

import com.zero.orzprofiler.profiler.router.common.RouterConstants;
import org.apache.log4j.Logger;

/**
 * User: luochao
 * Date: 13-12-4
 * Time: 上午11:02
 */
public class LoadBalanceFactory {
    private final static Logger log = Logger.getLogger(LoadBalanceFactory.class);
    private final static LoadBalanceFactory instance = new LoadBalanceFactory();

    private LoadBalanceFactory() {
    }
    public LoadBalanceStrategy getLoadPolicy(String policyType){
        if(RouterConstants.LB_RANDOM.equalsIgnoreCase(policyType)){
            return new RandomLoadBalance();
        }else if(RouterConstants.LB_ROUND_ROBIN.equalsIgnoreCase(policyType)){
            return new RoundRobinLoadBalance();
        }else if(RouterConstants.LB_ROUND_ROBIN.equalsIgnoreCase(policyType)){
            return new RoundRobinLoadBalance();
        }
        log.error("client pass the loadbalance strategy is not exist");
        return null;
    }
    public static LoadBalanceFactory getInstance(){
        return instance;
    }

}
