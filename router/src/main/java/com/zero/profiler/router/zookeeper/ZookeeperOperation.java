package com.zero.profiler.router.zookeeper;

import com.zero.profiler.router.exception.ZKCliException;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

/**
 * User: luochao
 * Date: 13-11-15
 * Time: 下午3:58
 */
public class ZookeeperOperation extends  ZookeeperRecyclableService implements ZookeeperService {
    //decorator
    private ZookeerRecyclableClient zkl = null;
    /*lose connection retry param*/
    public  int retryCount;
    public  int retryInterval;
    public ZookeeperOperation(ZookeeperProperties zookeeperProperties){
        super(zookeeperProperties.getServerList(),zookeeperProperties.getZkPoolSize());
        this.zkl = getZookeeperClient();
        this.retryCount = zookeeperProperties.getZkRetryCount();
        this.retryInterval = zookeeperProperties.getZkRetryInterval();
    }
    private boolean isAlive(){
        return zkl.getZk().getState().isAlive();
    }
    private boolean isConnected(){
        return  zkl.getZk().getState().equals(ZooKeeper.States.CONNECTED);
    }
    private boolean isConnecting(){
        return zkl.getZk().getState().equals(ZooKeeper.States.CONNECTING);
    }
    public void checkOpenConnecton() throws ZKCliException{
        if(isConnected()){
            return;
        }
        if(!isAlive()){
            int count = retryCount;
            while(count>0){
                this.reconnect();
                if(isConnected()){
                    return;
                }
                count--;

            }
        }
        if(isConnecting()){
            int count = retryCount;
            while (count>0){
                try {
                    Thread.sleep(retryInterval);
                }catch (Exception e){

                }
                count--;
            }
        }
        throw  new ZKCliException("Zookeeper connection is lose");
    }
    @Override
    public String getData(String path) throws ZKCliException {
        return null;
    }

    @Override
    public String getData(String path, AsyncCallback.DataCallback cb, Object ctx) throws ZKCliException {
        return null;
    }

    @Override
    public void setData(String path) throws ZKCliException {
    }

    @Override
    public void setData(String path, AsyncCallback.StatCallback cb, Object ctx) throws ZKCliException {
    }

    @Override
    public List<String> getChildren(String path) throws ZKCliException {
        return null;
    }

    @Override
    public List<String> getChildren(String path, AsyncCallback.ChildrenCallback cb, Object ctz) throws ZKCliException {
        return null;
    }

    @Override
    public void delete(String path) throws ZKCliException {
    }

    @Override
    public void delete(String path, boolean cascade, AsyncCallback.VoidCallback cb, Object ctx) throws ZKCliException {
    }

    @Override
    public void close() throws ZKCliException {
    }
}
