package com.zero.profiler.router.process;

import com.zero.profiler.router.Constants;
import com.zero.profiler.router.RouterException;
import com.zero.profiler.router.RouterService;
import com.zero.profiler.router.common.BrokerServiceUrls;
import com.zero.profiler.router.common.RouterConstants;
import com.zero.profiler.router.common.Util;
import com.zero.profiler.router.loadbalance.LoadBalanceContext;
import com.zero.profiler.router.loadbalance.RouterContext;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;

/**
 * User: luochao
 * Date: 13-12-3
 * Time: 下午2:37
 */
public class RouterHandler implements RouterService.Iface {
    private final static Logger log = Logger.getLogger(RouterHandler.class);
    private RouterContext routerContext;
    public RouterHandler(RouterContext routerContext) {
        this.routerContext = routerContext;
    }

    @Override
    public String getBroker(String user, String pwd, String topic, String apply, Map<String, String> prop) throws RouterException, TException {
        String clientId = prop.get(Constants.LOCAL_HOST)+ RouterConstants.ID_SPILT+topic;
        //客户端类型   PUB or PUB
        String type = prop.get(Constants.TYPE);
        LoadBalanceContext loadBalance = new LoadBalanceContext(routerContext.getPolicy(apply));
        log.info(String.format("One client request has been accept:threadId=%s clientId=%s user=%s pw=% topic=%s apply=%s prop=%s type=%s",
                Thread.currentThread().getId(),clientId,user, Util.getMD5(pwd),topic,apply,prop));
        String sessionId = null;
        try{
            if((sessionId=routerContext.authenticate(user,pwd,topic,prop)) != null){
                BrokerServiceUrls bru = new BrokerServiceUrls();
                bru.setSessionId(sessionId);
                List<String> serverList = new ArrayList<String>();
                if("PUB".equalsIgnoreCase(type)){
                    serverList.add(loadBalance.choose(topic,clientId));
                }else{
                    serverList.addAll(routerContext.getTopicBrokers(topic));
                }
                bru.setServerList(serverList);
                return Util.toJsonString(bru);
            }
        }catch (Exception e){

        }
        return null;
    }
}
