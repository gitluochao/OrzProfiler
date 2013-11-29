package com.zero.profiler.router.common;

/**
 * User: luochao
 * Date: 13-11-28
 * Time: 下午5:08
 */
public class BrokerUrl {
    private String id;
   	private String host;
   	private String external;
   	private String internal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getExternal() {
        return external;
    }

    public void setExternal(String external) {
        this.external = external;
    }

    public String getInternal() {
        return internal;
    }

    public void setInternal(String internal) {
        this.internal = internal;
    }
    public String getExternalUrl(){
        return host+":"+this.external;
    }
    public String getinternalUrl(){
        return host+":"+this.internal;
    }
}
