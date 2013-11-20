package com.zero.profiler.router.loadbalance;

import java.util.concurrent.ConcurrentHashMap;

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

    public RouterMap getInstance(){
        return  instance;
    }
}
