package org.apache.zookeeper.recipes;

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
 * Date: 13-11-22
 * Time: 下午2:05
 */
public class PathWatcher extends ProtocolSupport{
    private static final Logger log = Logger.getLogger(PathWatcher.class);
    private String path;
    private ZooKeeper zookeeper;
    private Watcher watcher;

    public PathWatcher(ZooKeeper zookeeper, String path, Watcher watcher) {
        super(zookeeper);
        this.path = path;
        this.zookeeper = zookeeper;
        this.watcher = watcher;
    }
    private class PathWatcherOperation implements ZooKeeperOperation<Boolean>,Watcher{
        private Semaphore semaphore = new Semaphore(1);
        @Override
        public void process(WatchedEvent event) {
            if(event.getType()==Event.EventType.None){
                if(event.getState() == Event.KeeperState.SyncConnected){
                    zookeeper.exists(path,watcher,null,null);
                    semaphore.release();
                }else{
                    zookeeper.exists(path,watcher,null,null);
                    semaphore.release();
                }
            }
        }

        @Override
        public Boolean execute() throws KeeperException, InterruptedException {
            semaphore.acquire();
            PathExistCheck pathExistCheck = new PathExistCheck(zookeeper,path,null);
            if(pathExistCheck.exist()){
                zookeeper.exists(path,watcher,null,null);
                return true;
            }else {
                log.error("path is not exist"+"["+path+"]");
                return false;
            }
        }
    }
    public Boolean watcher(){
        try{
           return this.retryOperation(new PathWatcherOperation());
        }catch (KeeperException e){
           log.error("lost connection to zookeeper",e);
        }catch (InterruptedException e){
           log.error("lost connection to zookeeper",e);
        }
        return false;
    }
}
