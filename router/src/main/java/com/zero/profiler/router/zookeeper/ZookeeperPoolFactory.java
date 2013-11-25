package com.zero.profiler.router.zookeeper;

import com.zero.profiler.router.common.Util;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * User: luochao
 * Date: 13-11-19
 * Time: 上午11:44
 */
public class ZookeeperPoolFactory {
   private final static Logger log = Logger.getLogger(ZookeeperPoolFactory.class);
   private List<ZookeeperExecute> pool;
   private final static ZookeeperPoolFactory instance = new ZookeeperPoolFactory();
   private int poolSize = 1;
   private int cursor = 0;//
   public ZookeeperPoolFactory(){
           init();
    }
    public static ZookeeperPoolFactory getInstance(){
        return instance;
    }
    public void init() {
        try {
            Properties prop = Util.getConf();
            ZookeeperProperties zkProp = new ZookeeperProperties(prop);
            int poolSize =zkProp.getZkPoolSize();
            pool = new ArrayList<ZookeeperExecute>(poolSize);
            for(int i=0;i<poolSize;i++){
                ZookeeperExecute zookeeperOperation = new ZookeeperExecute(zkProp);
                pool.add(zookeeperOperation);
            }
        } catch (Exception e) {
            log.error("load router.properties error", e);
        }
    }
    public synchronized ZookeeperExecute getZookeeperClient(){
        ZookeeperExecute zookeeperOperation = null;
        if(cursor<poolSize){
            zookeeperOperation = pool.get(cursor);
            cursor+=1;
        }else{
            cursor = 0;
            zookeeperOperation =  pool.get(cursor);
        }
        return zookeeperOperation;
    }

}
