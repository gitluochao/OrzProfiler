package com.zero.orzprofiler.profiler.router.common;

/**
 * User: luochao
 * Date: 13-11-28
 * Time: 下午6:00
 */
public class Session {
    //pub / sub
    private String type;
    private String timeout;
    private String subscriber;
    private String receiveWindowSize;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    public String getReceiveWindowSize() {
        return receiveWindowSize;
    }

    public void setReceiveWindowSize(String receiveWindowSize) {
        this.receiveWindowSize = receiveWindowSize;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String toString() {
        return "Session{" +
                "type='" + type + '\'' +
                ", timeout='" + timeout + '\'' +
                ", subscriber='" + subscriber + '\'' +
                ", receiveWindowSize='" + receiveWindowSize + '\'' +
                '}';
    }
}
