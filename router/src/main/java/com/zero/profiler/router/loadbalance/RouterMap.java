package com.zero.profiler.router.loadbalance;

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
    private final static ConcurrentHashMap<String,String> authMap = new ConcurrentHashMap<String, String>();


    public static RouterMap getInstance(){
        return  instance;
    }

}
