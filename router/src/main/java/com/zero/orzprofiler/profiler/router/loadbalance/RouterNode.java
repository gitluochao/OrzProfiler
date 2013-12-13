package com.zero.orzprofiler.profiler.router.loadbalance;

/**
 * User: luochao
 * Date: 13-11-19
 * Time: 上午11:10
 */
public class RouterNode {
    private String node;
    private String leaderNode;
    private String followerNode;

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getLeaderNode() {
        return leaderNode;
    }

    public void setLeaderNode(String leaderNode) {
        this.leaderNode = leaderNode;
    }

    public String getFollowerNode() {
        return followerNode;
    }

    public void setFollowerNode(String followerNode) {
        this.followerNode = followerNode;
    }
}
