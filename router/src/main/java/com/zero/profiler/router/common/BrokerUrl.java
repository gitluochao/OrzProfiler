package com.zero.profiler.router.common;

/**
 * User: luochao
 * Date: 13-11-28
 * Time: 下午5:08
 */
public class BrokerUrl  implements  Comparable<BrokerUrl>{
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

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof BrokerUrl){
            return this.id == ((BrokerUrl) obj).id && this.host == ((BrokerUrl) obj).host && this.internal == ((BrokerUrl) obj).internal
                    && this.external == ((BrokerUrl) obj).external;
        }
        return false;
    }

    @Override
    public String toString() {
        return "BrokerUrl{" +
                "id='" + id + '\'' +
                ", host='" + host + '\'' +
                ", external='" + external + '\'' +
                ", internal='" + internal + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public int compareTo(BrokerUrl o) {
       return  this.id.compareTo(o.id);
    }
}
