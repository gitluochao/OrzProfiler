package org.apache.zookeeper.recipes;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.recipes.lock.ProtocolSupport;
import org.apache.zookeeper.recipes.lock.ZooKeeperOperation;

/**
 * User: luochao
 * Date: 13-11-22
 * Time: 下午2:03
 */
public class PathDeleter extends ProtocolSupport{
    private final static Logger log = Logger.getLogger(PathDeleter.class);
    private String path;
    private ZooKeeper zookeeper;
    private PathVistor vistor;

    public PathDeleter(ZooKeeper zookeeper, String path) {
        super(zookeeper);
        this.path = path;
        this.zookeeper = zookeeper;
    }

    public PathDeleter(ZooKeeper zookeeper, String path, PathVistor vistor) {
        super(zookeeper);
        this.path = path;
        this.zookeeper = zookeeper;
        this.vistor = vistor;
    }

    private class PathDeleterOperation implements ZooKeeperOperation<Boolean>{
        boolean flag = false;
        @Override
        public Boolean execute() throws KeeperException, InterruptedException {
            PathExistCheck pathExistCheck = new PathExistCheck(zookeeper,path,null);
            if(vistor != null){
                if(pathExistCheck.exist()){
                    zookeeper.delete(path,pathExistCheck.getPathStat().getVersion(),vistor.getVoidCallback(),vistor.getCtx());
                    flag = true;
                }
            }else {
                if(pathExistCheck.exist()){
                    zookeeper.delete(path,pathExistCheck.getPathStat().getVersion(),null,null);
                    flag = true;
                }
            }
            return flag;
        }

    }
    private void delete(){
        try{
            this.retryOperation(new PathDeleterOperation());
        }catch (KeeperException e){
            log.error("lost connection to zookeeper ",e);
        }catch (InterruptedException e){
            log.error("exception cause by PathDeleter ",e);
        }

    }

}
