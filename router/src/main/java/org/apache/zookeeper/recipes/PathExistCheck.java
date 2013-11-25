package org.apache.zookeeper.recipes;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.recipes.lock.ProtocolSupport;
import org.apache.zookeeper.recipes.lock.ZooKeeperOperation;

/**
 * User: luochao
 * Date: 13-11-22
 * Time: 下午2:03
 */
public class PathExistCheck extends ProtocolSupport {
    private final static Logger log = Logger.getLogger(PathExistCheck.class);
    private String path;
    private ZooKeeper zookeeper;
    private PathVistor vistor;
    private Stat pathStat;

    public PathExistCheck(ZooKeeper zookeeper, String path,PathVistor vistor) {
        super(zookeeper);
        this.vistor = vistor;
        this.path = path;
        this.zookeeper = zookeeper;
    }

    private class PathExistCheckOperation implements ZooKeeperOperation<Boolean>,Watcher {
        private PathExistCheckOperation() {
        }

        @Override
        public void process(WatchedEvent event) {
           if(event.getType() == Event.EventType.None){
               log.info("connection lose...");
           }else {
               log.info("PathExistCheck some change occur on path"+"["+event.getPath()+"]");
           }

        }

        @Override
        public Boolean execute() throws KeeperException, InterruptedException {
            Stat stat = null;
            if(vistor!=null){
                stat = zookeeper.exists(path,vistor.getWatcher());
            }else{
                stat = zookeeper.exists(path,this);
            }
            if(stat == null){
                return false;
            }
            pathStat = stat;
            return true;
        }
    }
    public boolean exist(){
       try{
            return  this.retryOperation(new PathExistCheckOperation());
       }catch (KeeperException e){
           log.error("PathExistCheck exception",e);
       }catch (InterruptedException e){
           log.error("path exist check exception",e);
       }
       return  false;
    }

    public Stat getPathStat() {
        return pathStat;
    }
}
