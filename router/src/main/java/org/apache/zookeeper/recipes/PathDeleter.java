package org.apache.zookeeper.recipes;

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
    private String path;
    private ZooKeeper zooKeeper;
    private PathVistor vistor;

    public PathDeleter(ZooKeeper zookeeper, String path, ZooKeeper zooKeeper) {
        super(zookeeper);
        this.path = path;
        this.zooKeeper = zooKeeper;
    }
    private class PathDeleterOperation implements ZooKeeperOperation<Boolean>{
        boolean flag = false;
        @Override
        public Boolean execute() throws KeeperException, InterruptedException {
            PathExistCheck pathExistCheck = new PathExistCheck(zookeeper,path,null);
            if(vistor != null){
                if(pathExistCheck.exist()){
                    zooKeeper.delete(path,pathExistCheck.getPathStat().getVersion(),vistor.getVoidCallback(),vistor.getCtx());
                    flag = true;
                }
            }else {
                if(pathExistCheck.exist()){
                    zooKeeper.delete(path,pathExistCheck.getPathStat().getVersion(),null,null);
                    flag = true;
                }
            }
            return flag;
        }

    }

}
