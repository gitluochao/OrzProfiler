package com.zero.profiler.router.zookeeper;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.recipes.lock.ProtocolSupport;
import org.apache.zookeeper.recipes.lock.ZooKeeperOperation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * User: luochao
 * when ChildNode changed update keeperSize
 * Date: 13-10-30
 * Time: 涓嫔崃4:32
 */
public class PathChildWatcher extends ProtocolSupport {
    private static final Logger log = Logger.getLogger(PathChildWatcher.class);
    private final String path;
    private final ZooKeeper zk;
    private final Visitor vistor;

    public PathChildWatcher(String path, ZooKeeper zk, Visitor vistor) {
        super(zk);
        this.path = path;
        this.zk = zk;
        this.vistor = vistor;
    }
    private class WatcherOperation implements ZooKeeperOperation<Boolean>,Watcher {
        private Semaphore semaphore = new Semaphore(1);
        private Event.EventType eventType;
        private String eventPath;
        private  Map<String,List<String>> prveMap = new ConcurrentHashMap<String, List<String>>();
        @Override
        public void process(WatchedEvent event) {
            eventType = event.getType();
            eventPath = event.getPath();
            if(eventType == Event.EventType.None){
                if(event.getState() == Event.KeeperState.SyncConnected){
                    log.info("lose connnect to zookeeper");
                    semaphore.release();
                }
            }else if(eventType == Event.EventType.NodeChildrenChanged || eventType == Event.EventType.NodeDataChanged){
                log.info("some change occur on Child node");
                semaphore.release();
            }
        }

        @Override
        public Boolean execute() throws InterruptedException {
             while (true){
                semaphore.acquire();
                List<String> childrens = null;
                if(eventType == Event.EventType.None){
                    log.info("path change"+path);
                    if(prveMap.isEmpty()){
                        try{
                            childrens = zk.getChildren(path,this);
                        }catch (KeeperException e){
                            log.error("error: get child info from zookeeper",e);
                        }
                        prveMap.put(path,childrens);
                    }else{
                        for(String recoverPath : prveMap.keySet()){
                            try{
                                childrens = zk.getChildren(recoverPath,this);
                            }catch (KeeperException e){
                                log.error("error : recover  child info from zookeeper",e);
                            }
                            prveMap.put(path,childrens);
                        }
                    }
                }else {
                    //znode鎹彂鐢熷彉鍖?
                    log.info("znode child data changed :"+eventType);
                    int prvCount = 0;
                    int newCount = 0;
                    try{
                        childrens = zk.getChildren(eventPath,this);
                        if(childrens != null){
                            newCount = childrens.size();
                        }
                    }catch (KeeperException e){
                        log.error("error: get child info "+eventPath);
                    }
                    List<String> prvData = prveMap.get(eventPath);
                    if(prvData!=null)
                        prvCount = 0;
                    childrens.removeAll(prvData);
                    if(childrens != null ){
                        newCount = childrens.size();
                    }
                    if(newCount > prvCount){
                        log.debug("some new child added");
                        List<String> newChildrens = null;
                        for(String childPath : childrens){
                           try {
                               newChildrens = zk.getChildren(childPath,this);
                           }catch (KeeperException e){
                               log.error("get new children's error");
                           }
                            if(newChildrens != null){
                                prveMap.put(childPath,newChildrens);
                            }
                        }
                    }
                    prveMap.put(eventPath,childrens);
                    if(eventType == Event.EventType.NodeChildrenChanged){
                          vistor.onNodeChildrenChanged(eventPath,childrens);
                    }
                }
             }
        }
    }
    public void watcher(){
        try {
            super.retryOperation(new WatcherOperation());
        }catch (KeeperException e){
             log.error("zk exception",e);
        }catch (InterruptedException e){

        }
    }

}
