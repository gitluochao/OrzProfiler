package com.zero.profiler.router.common;

/**
 * User: luochao
 * Date: 13-10-29
 * Time: 涓嫔崃6:17
 */
public abstract class ParamKey {
    //zkService Params
    public static class ZKService{
       public static final String hosts = "ZK_HOST_LIST";
       public static final String timeOut = "ZK_TIME_OUT";
       public static final String poolSize = "ZK_POOL_SIZE";

    }
    //zkClient Params
    public static class ZKClient{
       public static final String zkClientSize= "ZK_CLIENT_SIZE"; //zk pool size
       public static final String zkRetryCout = "ZK_RETRY_COUNT";
       public static final String zkRetryInterval = "ZK_RETRY_INTERVAL";
    }
    //ZNode info
    public static class ZNode{
        public static final String user = "/user";
        public static final String topic = "/categories";
        public static final String session = "/clients";
        public static final String broker = "/brokers";
        public static final String status = "/status";
    }
    //server param
    public static class Server{
        public static final String bindAdr = "BIND_ADR";
        public static final String port = "PORT";
        public static final String maxWorkerThreads  = "MAX_WORK_THREADS";
        public static final String minWorkerThreads  = "MAX_WORKER_THREADS";
        public static final String stopTimeoutUnit  = "TIME_UNIT";
        public static final String stopTimeoutVal = "STOP_TIMEOUT_VAL";
        public static final String cliTimeout = "CLI_TIMEOUT";
    }
}
