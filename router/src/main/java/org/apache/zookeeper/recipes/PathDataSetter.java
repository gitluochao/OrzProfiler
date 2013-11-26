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
public class PathDataSetter extends ProtocolSupport{
    private final static Logger log = Logger.getLogger(PathDataSetter.class);
    private String path;
    private ZooKeeper zookeeper;
    private byte[] data;

    public PathDataSetter(ZooKeeper zookeeper, String path,byte[] data) {
        super(zookeeper);
        this.path = path;
        this.zookeeper = zookeeper;
        this.data = data;
    }
    private class PathDataSetterOperation implements ZooKeeperOperation<Boolean>{
        private PathDataSetterOperation() {
        }

        @Override
        public Boolean execute() throws KeeperException, InterruptedException {
            PathExistCheck pathExistCheck = new PathExistCheck(zookeeper,path,null);
            if(pathExistCheck.exist()){
                zookeeper.setData(path,data,pathExistCheck.getPathStat().getVersion());
                return true;
            }else {
                log.error("path ["+path+"] is not exist");
            }
            return false;
        }
    }
    public Boolean setData(){
        try{
             return this.retryOperation(new PathDataSetterOperation());
        }catch (KeeperException e){
            log.error("lose connnect to zookeeper ",e);
        }catch (InterruptedException e){
            log.error("exception cause by PathDataSetter",e);
        }
        return  false;
    }

}
