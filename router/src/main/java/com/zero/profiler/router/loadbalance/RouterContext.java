package com.zero.profiler.router.loadbalance;

import com.zero.profiler.router.common.*;
import com.zero.profiler.router.service.ServerEngine;
import com.zero.profiler.router.service.ServerEngineFactory;
import com.zero.profiler.router.zookeeper.Visitor;
import com.zero.profiler.router.zookeeper.ZookeeperExecute;
import com.zero.profiler.router.zookeeper.ZookeeperPoolFactory;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: luochao
 * Date: 13-11-1
 * Time: 下午6:02
 */
public class RouterContext implements Context,Visitor{
    private final static Logger log = Logger.getLogger(RouterContext.class);
    private Properties appParam;
    private ZookeeperPoolFactory zkFactory = ZookeeperPoolFactory.getInstance();
    private AtomicBoolean syning = new AtomicBoolean(false);
    private RouterMap routerMap = RouterMap.getInstance();
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
               ZookeeperExecute zkClient = zkFactory.getZookeeperClient();
               List<String> topics = zkClient.getChildren(ParamKey.ZNode.topic);

               if(topics != null && topics.size()>0){
                    Set<String> newBrokers = new HashSet<String>();
                    for(String topic:topics){
                        //当前topic下面的所有broker
                        List<BrokerUrl> serviceUrls = new ArrayList<BrokerUrl>();
                        String topicConf = zkClient.getData(ParamKey.ZNode.topic+"/"+topic);
                        Topic topicObject = (Topic)Util.parseObject(topicConf,Topic.class);
                        String groupName = topicObject.getGroup();
                        List<String> brokerGroups = null;
                        //get the broker information of the topic
                        if(groupName != null){
                            brokerGroups = zkClient.getChildren(ParamKey.ZNode.broker+"/"+groupName);
                        }
                        if(brokerGroups != null && brokerGroups.size()>0) {
                             for(String broker : brokerGroups){
                                  String brokerStr = zkClient.getData(ParamKey.ZNode.broker+"/"+groupName+"/"+broker);
                                  BrokerUrl brokerUrl = (BrokerUrl)Util.parseObject(brokerStr,BrokerUrl.class);
                                  serviceUrls.add(brokerUrl);
                                  newBrokers.add(broker);
                             }

                        }
                    }
               }


            }
        }catch (Exception e){
            log.error(e.toString());
       					if (exitFlag)
       						System.exit(-1);
        }finally {
            syning.set(false);
        }

    }
    private void synRouter(boolean exitFlag){
        try{
            if(syning.compareAndSet(false,true)){
                 ZookeeperExecute zkClient = zkFactory.getZookeeperClient();

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
