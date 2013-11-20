package com.zero.profiler.router.loadbalance;

import com.zero.profiler.router.common.ParamKey;
import com.zero.profiler.router.common.RouterConstants;
import com.zero.profiler.router.common.Util;
import com.zero.profiler.router.service.ServerEngine;
import com.zero.profiler.router.service.ServerEngineFactory;
import com.zero.profiler.router.zookeeper.Visitor;
import com.zero.profiler.router.zookeeper.ZookeeperOperation;
import com.zero.profiler.router.zookeeper.ZookeeperPoolFactory;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: luochao
 * Date: 13-11-1
 * Time: 下午6:02
 */
public class RouterContext implements Context,Visitor{
    private Properties appParam;
    private ZookeeperPoolFactory zkFactory = ZookeeperPoolFactory.getInstance();
    private AtomicBoolean syning = new AtomicBoolean(false);
    private String engineType;
    public RouterContext() {
        init();
    }

    //lazy factory
   private static class Holder{
       static  final RouterContext routerContext = new RouterContext();
   }
   public static RouterContext getRouterContext(){
       return Holder.routerContext;
   }
   private void init(){
       try{
           appParam = Util.getConf();
           engineType = Util.getStrParam(ParamKey.Server.serverType,appParam.getProperty(ParamKey.Server.serverType), RouterConstants.DEFAULT_SEVER_TYPE,true);

       }catch (Exception e){

       }

   }


    @Override
    public void syn() {
        boolean exitFlag = false;
        try {
            if(syning.compareAndSet(false,true)){
                String engineClass = ParamKey.Server.ServerClass.valueOf(engineType).getClassName();
                ServerEngine engine = ServerEngineFactory.getInstance(engineClass);
                exitFlag = !engine.isStarted();
            }
        }catch (Exception e){

        }finally {
            syning.set(false);
        }
        synTopic(exitFlag);
        synRouter(exitFlag);
        synAuthInfo(exitFlag);

    }

    @Override
    public void clean() {

    }
    private void synTopic(boolean exitFlag){
        try{
            if(syning.compareAndSet(false,true)){

            }
        }catch (Exception e){

        }finally {
            syning.set(false);
        }

    }
    private void synRouter(boolean exitFlag){
        try{
            if(syning.compareAndSet(false,true)){
                 ZookeeperOperation  zkClient = zkFactory.getZookeeperClient();

            }
        }catch (Exception e){

        }finally {
            syning.set(false);
        }

    }
    private void synAuthInfo(boolean exitFlag){
        try{
            if(syning.compareAndSet(false,true)){

            }
        }catch (Exception e){

        }finally {
            syning.set(false);
        }
    }
    @Override
    public void onNodeChildrenChanged(String path, List<String> children) {

    }

    @Override
    public void onNodeCreated(String path) {

    }

    @Override
    public void onNodeDataChanged(String path) {

    }

    @Override
    public void onNodeDeleted(String path) {

    }
}
