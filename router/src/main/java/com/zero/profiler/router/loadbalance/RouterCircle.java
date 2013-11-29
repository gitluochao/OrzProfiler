package com.zero.profiler.router.loadbalance;

import com.zero.profiler.router.common.BrokerUrl;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * User: luochao
 * Date: 13-11-19
 * Time: 上午11:10
 */
public class RouterCircle {
    private Map<String ,RouterNode> circle = new HashMap<String, RouterNode>();
    private String topic;
    private String cursor;
    private Map<String,RouterNode> createCircle(List<BrokerUrl> brokerUrls){
        return  circle;
    }
}
