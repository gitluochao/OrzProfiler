package org.apache.zookeeper.recipes;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.recipes.lock.ProtocolSupport;
import org.apache.zookeeper.recipes.lock.ZooKeeperOperation;

/**
 * User: luochao
 * Date: 13-11-22
 * Time: 下午2:04
 */
public class PathDataGetter extends ProtocolSupport {
    private final static Logger log = Logger.getLogger(PathDataGetter.class);
    private String path;
    private ZooKeeper zookeeper;
    private PathVistor vistor;

    public PathDataGetter(ZooKeeper zookeeper, String path, PathVistor vistor) {
        super(zookeeper);
        this.path = path;
        this.zookeeper = zookeeper;
        this.vistor = vistor;
    }

    public PathDataGetter(ZooKeeper zookeeper, String path) {
        super(zookeeper);
        this.path = path;
        this.zookeeper = zookeeper;
    }

    private class PathDataGetterOperation implements ZooKeeperOperation<byte[]>{
        private PathDataGetterOperation() {
        }
        @Override
        public byte[] execute() throws KeeperException, InterruptedException {
            if(vistor!=null){
                return zookeeper.getData(path,vistor.getWatcher(),vistor.getStat());
            }else{
                return zookeeper.getData(path,false,null);
            }
        }
    }
    public byte[] getData(){
        try{
            return this.retryOperation(new PathDataGetterOperation());
        }catch (KeeperException e){
            log.error("lose connection to zookeeper cluster");
        }catch (InterruptedException e){
            log.error("lose connection to zookeeper cluster");
        }
        return null;
    }
}
