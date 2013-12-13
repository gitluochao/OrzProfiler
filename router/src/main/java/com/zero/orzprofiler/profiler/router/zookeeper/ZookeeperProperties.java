package com.zero.orzprofiler.profiler.router.zookeeper;

import com.zero.orzprofiler.profiler.router.common.ParamKey;
import com.zero.orzprofiler.profiler.router.common.Util;
import org.apache.log4j.Logger;

import java.util.Properties;

/**
 * User: luochao
 * Date: 13-11-1
 * Time: 涓嫔崃2:06
 */
public class ZookeeperProperties {
    private static final Logger log = Logger.getLogger(ZookeeperProperties.class);
    private String serverList;
    private int zkTimeOut;
    private int zkPoolSize;
    private int zkRetryCount;
    private int zkRetryInterval;

    public ZookeeperProperties(Properties prop) {
        if(prop != null){
            try{
                    this.serverList = Util.getStrParam(ParamKey.ZKService.hosts,prop.getProperty(ParamKey.ZKService.hosts),Util.getHostName(),true);
                    this.zkTimeOut = Util.getIntParam(ParamKey.ZKService.timeOut,prop.getProperty(ParamKey.ZKService.timeOut),3000,3000,500000,true);
                    this.zkPoolSize = Util.getIntParam(ParamKey.ZKService.poolSize,prop.getProperty(ParamKey.ZKService.poolSize),1,1,500);
                    this.zkRetryCount = Util.getIntParam(ParamKey.ZKClient.zkRetryCout,prop.getProperty(ParamKey.ZKClient.zkRetryCout),1,1,20);
                    this.zkRetryInterval = Util.getIntParam(ParamKey.ZKClient.zkRetryInterval,prop.getProperty(ParamKey.ZKClient.zkRetryInterval),1000,10,1000);
               }catch (Exception e){
                    log.error("parse param error...");
                    System.exit(-1);
               }
        }
    }

    public String getServerList() {
        return serverList;
    }

    public void setServerList(String serverList) {
        this.serverList = serverList;
    }

    public int getZkTimeOut() {
        return zkTimeOut;
    }

    public void setZkTimeOut(int zkTimeOut) {
        this.zkTimeOut = zkTimeOut;
    }

    public int getZkPoolSize() {
        return zkPoolSize;
    }

    public void setZkPoolSize(int zkPoolSize) {
        this.zkPoolSize = zkPoolSize;
    }

    public int getZkRetryCount() {
        return zkRetryCount;
    }

    public void setZkRetryCount(int zkRetryCount) {
        this.zkRetryCount = zkRetryCount;
    }

    public int getZkRetryInterval() {
        return zkRetryInterval;
    }

    public void setZkRetryInterval(int zkRetryInterval) {
        this.zkRetryInterval = zkRetryInterval;
    }
}
