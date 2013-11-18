package com.zero.profiler.router.zookeeper;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.recipes.lock.ProtocolSupport;
import org.apache.zookeeper.recipes.lock.ZooKeeperOperation;

import java.util.concurrent.Semaphore;

/**
 * User: luochao
 * Date: 13-10-30
 * Time: 涓嫔崃4:32
 */
public class PathExistWatcher extends ProtocolSupport{
    private final static Logger log = Logger.getLogger(PathExistWatcher.class);
    private final ZooKeeper zookeeper;
    private final String path;
    private final Visitor visitor;

    public PathExistWatcher(ZooKeeper zookeeper, String path, Visitor visitor) {
        super(zookeeper);
        this.zookeeper = zookeeper;
        this.path = path;
        this.visitor = visitor;
    }

    private class WatchDataOperation implements ZooKeeperOperation,Watcher {
        private Semaphore semaphore = new Semaphore(1);
        private Event.EventType eventType;
        private String eventPath;
        @Override
        public void process(WatchedEvent event) {
            eventType = event.getType();
            eventPath = event.getPath();
            if(eventType == Event.EventType.None){
                if(event.getState()== Event.KeeperState.SyncConnected){
                    semaphore.release();
                }

            }else if(eventType == Event.EventType.NodeCreated||eventType == Event.EventType.NodeDeleted){
                semaphore.release();
            }
        }

        @Override
        public Object execute() throws KeeperException, InterruptedException {
            while (true){
                semaphore.acquire();
                if(eventPath == null){
                    zookeeper.exists(path,this);
                }else {
                    zookeeper.exists(eventPath,this);
                }
                if(eventType == Event.EventType.NodeCreated){
                    visitor.onNodeCreated(eventPath);
                }else if(eventType == Event.EventType.NodeDeleted){
                    visitor.onNodeDeleted(eventPath);
                }else if(eventType == Event.EventType.NodeDataChanged){
                    visitor.onNodeDataChanged(path);
                }
            }
        }
    }
    public void watcher(){
        try{
            super.retryOperation(new WatchDataOperation());
        }catch (KeeperException e){
            log.error("watch exist path exception",e);
        }catch (InterruptedException e){
            log.error("thread interruptedException ",e);
        }
    }
}
