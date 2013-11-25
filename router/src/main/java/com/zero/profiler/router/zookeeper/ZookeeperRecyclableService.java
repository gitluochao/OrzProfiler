package com.zero.profiler.router.zookeeper;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
/**
 * User: luochao
 * Date: 13-10-30
 * Time: 下午 2:36
 *
 */
public abstract class ZookeeperRecyclableService  implements Watcher {
    private final static Logger log = Logger.getLogger(ZookeeperRecyclableService.class);
    private ZooKeeper zooKeeper;
    private ZookeerRecyclableClient zookeeperClient;
    private String serverList;
    private int sessionTimeOut;
    private CountDownLatch connectedSign = new CountDownLatch(1);
    private DataMonitorListener listener;


    public void setConnectedSign(CountDownLatch connectedSign) {
        this.connectedSign = connectedSign;
    }

    protected ZookeeperRecyclableService(String serverList,int sessionTimeOut) {
        this(serverList, sessionTimeOut,null);
    }

    protected ZookeeperRecyclableService(String serverList, int sessionTimeOut, DataMonitorListener listener) {
        this.serverList = serverList;
        this.sessionTimeOut = sessionTimeOut;
        this.listener = listener;
        this.connect();
    }
    private void connect(){
        try{
            zooKeeper = new ZooKeeper(serverList,sessionTimeOut,this);
            zookeeperClient = new ZookeerRecyclableClient(zooKeeper);
            connectedSign.await();
        }catch (IOException e){
            throw new RuntimeException("fail connnect to zookeeper cluster"+serverList,e);
        }catch (InterruptedException ex){
            Thread.currentThread().interrupt();
        }
    }
    public void reconnect(){
        try{
            zooKeeper = new ZooKeeper(serverList,sessionTimeOut,this);
            zookeeperClient.reconnect(zooKeeper);
            connectedSign.await();
        }catch (IOException e){
            throw new RuntimeException("fail  reconnnect to zookeeper cluster"+serverList,e);
        }catch (InterruptedException ex){
            Thread.currentThread().interrupt();
        }
    }
    @Override
    public void process(WatchedEvent event) {
        //watch rpc
        if(event.getType() == Event.EventType.None ) {
            if(event.getState() == Event.KeeperState.SyncConnected){
                log.info("connection .....ok");
                connectedSign.countDown();
            }else if (event.getState() == Event.KeeperState.Expired){
                log.info("lose connect to cluster permenantly");
                this.setConnectedSign(new CountDownLatch(1));
                reconnect();
            }
        }else{
            processEnventData(event);
        }

    }
    public void processEnventData(WatchedEvent event){
        if(listener!=null){
            switch (event.getType()){
                case NodeCreated:
                    listener.onNodeCreated(event.getPath());
                    break;
                case NodeDataChanged:
                    listener.onNodeDataChanged(event.getPath());
                    break;
                case NodeChildrenChanged:
                    listener.onNodeChildChanged(event.getPath());
                    break;
                case NodeDeleted:
                    listener.onNodeDeleted(event.getPath());
                    break;
                default:break;
            }
        }

    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public ZookeerRecyclableClient getZookeeperClient() {
        return zookeeperClient;
    }

    public void setZookeeperClient(ZookeerRecyclableClient zookeeperClient) {
        this.zookeeperClient = zookeeperClient;
    }

    public interface  DataMonitorListener{

        void onNodeDataChanged(String path);

        void onNodeCreated(String path);

        void onNodeChildChanged(String path);

        void onNodeDeleted(String path);

    }
}
