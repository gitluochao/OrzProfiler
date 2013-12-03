package com.zero.profiler.router.loadbalance;

import com.zero.profiler.router.common.BrokerUrl;

import java.util.*;

/**
 * User: luochao
 * Date: 13-11-19
 * Time: 上午11:10
 */
public class RouterCircle {
    private Map<String ,RouterNode> circle = new HashMap<String, RouterNode>();
    private String topic;
    private int count;
    private int startIndex;
    private String cursor;

    public RouterCircle(String topic) {
        this(topic, 0);
    }

    public RouterCircle(String topic, int startIndex) {
        this.topic = topic;
        this.startIndex = startIndex;
        this.count = 0;
    }
    public List<String>  getNodes(){
        return new ArrayList<String>(circle.keySet());
    }

    public synchronized String getFollowerNode(){
        RouterNode node = circle.get(cursor);
        cursor = node.getFollowerNode();
        return  cursor;
    }
    public String getCurrentNode(){
        RouterNode routerNode = circle.get(cursor);
        return routerNode.getNode();
    }
    public  Map<String,RouterNode> createCircle(List<BrokerUrl> brokerUrls){
        if(brokerUrls == null || brokerUrls.size() == 0){
            return null;
        }
        count = brokerUrls.size();
        Collections.sort(brokerUrls);
        //construct broker Cirle
        for(int i = 0;i < count;i++){
            RouterNode node = new RouterNode();
            node.setNode(brokerUrls.get(i).getExternal());
            if(startIndex == i){
                cursor = node.getNode();
            }
            if(i == 0){
                if(count == 1){
                    node.setFollowerNode(brokerUrls.get(i).getExternal());
                    node.setLeaderNode(brokerUrls.get(i).getExternal());
                }

            }
            if(i > 0){
               if(i < count-1){
                   node.setLeaderNode(brokerUrls.get(i-1).getExternal());
                   circle.get(brokerUrls.get(i-1).getExternal()).setFollowerNode(node.getNode());
                   node.setFollowerNode(brokerUrls.get(i).getExternal());
               }
               if(i == count -1){
                   node.setLeaderNode(brokerUrls.get(i-1).getExternal());
                   circle.get(brokerUrls.get(i-1).getExternal()).setFollowerNode(node.getNode());
                   node.setFollowerNode(brokerUrls.get(0).getExternal());
                   circle.get(brokerUrls.get(0).getExternal()).setLeaderNode(node.getNode());
               }
            }
            circle.put(brokerUrls.get(i).getExternal(),node);
        }
        return  circle;
    }

}
