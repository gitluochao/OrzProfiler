package com.zero.orzprofiler.profiler.router.loadbalance;

import com.zero.orzprofiler.profiler.router.common.BrokerUrl;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

/**
 * User: luochao
 * Date: 13-11-19
 * Time: 上午11:10
 */
public class RouterMap {
    private final static  RouterMap instance = new RouterMap();
    /**
     * topic / broker circle
     */
    private final static ConcurrentHashMap<String,RouterCircle> routerMap = new ConcurrentHashMap<String, RouterCircle>();
    //preserve the broker which server the client
    private final static ConcurrentHashMap<String,String> clientMap = new ConcurrentHashMap<String, String>();


    public static RouterMap getInstance(){
        return  instance;
    }
    public synchronized void update(String topic,List<BrokerUrl> serviceUrls){
        if(topic == null || serviceUrls.size() == 0){
            return;
        }
        RouterCircle routerCircle = new RouterCircle(topic);
        routerCircle.createCircle(serviceUrls);
        routerMap.put(topic,routerCircle);
    }
    public synchronized void changeClientInfo(Set<String> newServer){
        clientMap.values().retainAll(newServer);
    }
    public void cleanAll(){
        if(routerMap.size() > 0 ){
            routerMap.clear();
        }
        if(clientMap.size() > 0){
            clientMap.clear();
        }
    }
    public String getFollower(String topic){
       RouterCircle circle = routerMap.get(topic);
       return circle.getFollowerNode();
    }
    public List<String> getBrokers(String topic){
       RouterCircle circle = routerMap.get(topic);
       return circle.getNodes();
    }
    public String getExistClientBroker(String client){
        return clientMap.get(client);
    }
    public void setExistClientBroker(String clientId,String broker){
        clientMap.put(clientId,broker);
    }

}
