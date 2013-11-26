package com.zero.profiler.router.loadbalance;

import java.util.HashMap;
import java.util.Map;

/**
 * User: luochao
 * Date: 13-11-19
 * Time: 上午11:10
 */
public class RouterCircle {
    private Map<String ,RouterNode> circle = new HashMap<String, RouterNode>();
    private String topic;
    private String cursor;
}
