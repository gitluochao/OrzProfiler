package com.zero.orzprofiler.profiler.router.common;

import java.util.List;

/**
 * User: luochao
 * Date: 13-12-4
 * Time: 下午2:22
 */
public class BrokerServiceUrls {
    private String sessionId;
    private List<String> serverList;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<String> getServerList() {
        return serverList;
    }

    public void setServerList(List<String> serverList) {
        this.serverList = serverList;
    }
}
