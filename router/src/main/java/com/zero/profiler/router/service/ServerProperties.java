package com.zero.profiler.router.service;

import com.zero.profiler.router.common.ParamKey;
import com.zero.profiler.router.common.Util;
import com.zero.profiler.router.exception.ValidationException;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * User: luochao
 * Date: 13-11-18
 * Time: 上午10:09
 */
public class ServerProperties {
    private Logger log = Logger.getLogger(ServerProperties.class);
    private String id = "service properties id";
    private String name = "service name";
    /**
     * thrift bind ip
     */
    private String bindadr = "localhost";
    /**
     * thrift bind port
     */
    private int port = 9990;
    /**
     * the max num of pool server's thread
     */
    private int maxWorkThreads = Integer.MAX_VALUE;
    /**
     * the min num of pool server's thread
     */
    private int minWorkThreads = 1;
    /**
     * 时间单元
     */
    private TimeUnit stopTimeoutUnit = TimeUnit.SECONDS;
    /**
     *
     */
    private int stopTimeoutVal = 60;
    /**
     * 连接超时时间
     */
    private int cliTimeout = 5000;

    private long maxReadBufferBytes = Long.MAX_VALUE;

    public ServerProperties(Properties prop) throws ValidationException{
        bindadr = prop.getProperty(ParamKey.Server.bindAdr);
        if(bindadr == null||bindadr.isEmpty()){
           bindadr = Util.getHostName();
        }

        port = Util.getIntParam(ParamKey.Server.port,prop.getProperty(ParamKey.Server.port),9990,1025,65534);
        minWorkThreads = Util.getIntParam(ParamKey.Server.minWorkerThreads,prop.getProperty(ParamKey.Server.minWorkerThreads),60,0,1000);
        maxWorkThreads = Util.getIntParam(ParamKey.Server.maxWorkerThreads,prop.getProperty(ParamKey.Server.maxWorkerThreads),1000,10,10000);
        if(minWorkThreads > maxWorkThreads){
           throw  new ValidationException(String.format("minWorkThreads:[%d] is greater than maxWorkThreads:[%d]",minWorkThreads,maxWorkThreads));
        }
        stopTimeoutUnit = TimeUnit.valueOf(prop.getProperty(ParamKey.Server.stopTimeoutUnit, "SECONDS"));
        stopTimeoutVal = Util.getIntParam(ParamKey.Server.stopTimeoutVal,prop.getProperty(ParamKey.Server.stopTimeoutVal),60,0,10000);
        cliTimeout = Util.getIntParam(ParamKey.Server.cliTimeout,prop.getProperty(ParamKey.Server.cliTimeout),5000,0,500000);
        maxReadBufferBytes = Util.getLongParam(ParamKey.Server.maxReadBuffer,prop.getProperty(ParamKey.Server.maxReadBuffer));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBindadr() {
        return bindadr;
    }

    public int getPort() {
        return port;
    }

    public int getMaxWorkThreads() {
        return maxWorkThreads;
    }

    public int getMinWorkThreads() {
        return minWorkThreads;
    }

    public TimeUnit getStopTimeoutUnit() {
        return stopTimeoutUnit;
    }

    public int getStopTimeoutVal() {
        return stopTimeoutVal;
    }

    public int getCliTimeout() {
        return cliTimeout;
    }

    public long getMaxReadBufferBytes() {
        return maxReadBufferBytes;
    }
}
