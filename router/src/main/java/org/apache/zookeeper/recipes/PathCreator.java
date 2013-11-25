package org.apache.zookeeper.recipes;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.recipes.lock.ProtocolSupport;
import org.apache.zookeeper.recipes.lock.ZooKeeperOperation;
/**
 * User: luochao
 * Date: 13-11-22
 * Time: 下午2:03
 */
public class PathCreator extends ProtocolSupport {
    private String path;
    private ZooKeeper zookeeper;
    private CreateMode createMode;
    private PathVistor vistor;

    public PathCreator(ZooKeeper zookeeper, String path,  CreateMode createMode, PathVistor vistor) {
        super(zookeeper);
        this.path = path;
        this.zookeeper = zookeeper;
        this.createMode = createMode;
        this.vistor = vistor;
    }

    //
    private class CreatePathOperation implements ZooKeeperOperation<Boolean> {
        private CreatePathOperation() {
        }

        @Override
        public Boolean execute() throws KeeperException, InterruptedException {
            boolean flag = false;
            if(vistor!=null){
                zookeeper.create(path,vistor.getData(),vistor.getAcls(),createMode,vistor.getStringCallback(),vistor.getCtx());
                flag = true;
            }else{
                zookeeper.create(path,null,null,createMode,null,null);
                flag = true;
            }
            return flag;
        }
    }
    public void create() throws KeeperException,InterruptedException{
        this.retryOperation(new CreatePathOperation());
    }
}
