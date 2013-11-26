package org.apache.zookeeper.recipes;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.recipes.lock.ProtocolSupport;
import org.apache.zookeeper.recipes.lock.ZooKeeperOperation;
import java.util.List;
/**
 * User: luochao
 * Date: 13-11-26
 * Time: 下午6:38
 */
public class PathChildrenGetter extends ProtocolSupport {
    private ZooKeeper zookeeper;
    private String path;
    private PathVistor vistor;

    public PathChildrenGetter(ZooKeeper zookeeper,  String path, PathVistor vistor) {
        super(zookeeper);
        this.zookeeper = zookeeper;
        this.path = path;
        this.vistor = vistor;
    }

    public PathChildrenGetter(ZooKeeper zookeeper, String path) {
        super(zookeeper);
        this.zookeeper = zookeeper;
        this.path = path;
    }
    private class PathChildrenGetterOperation implements ZooKeeperOperation<List<String>>{
        private PathChildrenGetterOperation() {
        }

        @Override
        public List<String> execute() throws KeeperException, InterruptedException {
            if(vistor!=null){
                return zookeeper.getChildren(path,vistor.getWatcher());
            }else {
                return zookeeper.getChildren(path,false);
            }
        }
    }
    public List<String> getChildren(){
        try{
        return this.retryOperation(new PathChildrenGetterOperation());
        }catch (KeeperException e){

        }catch (InterruptedException e){

        }
        return null;
    }
}
