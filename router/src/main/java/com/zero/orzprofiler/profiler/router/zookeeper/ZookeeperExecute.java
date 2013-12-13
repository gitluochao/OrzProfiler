package com.zero.orzprofiler.profiler.router.zookeeper;

import com.zero.orzprofiler.profiler.router.exception.ZKCliException;
import org.apache.log4j.Logger;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.recipes.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * User: luochao
 * Date: 13-11-15
 * Time: 下午3:58
 */
public class ZookeeperExecute extends  ZookeeperRecyclableService implements ZookeeperService {
    private final static Logger log = Logger.getLogger(ZookeeperExecute.class);
    //decorator
    private ZookeerRecyclableClient zkl = null;
    /*lose connection retry param*/
    public  int retryCount;
    public  int retryInterval;
    public ZookeeperExecute(ZookeeperProperties zookeeperProperties){
        super(zookeeperProperties.getServerList(),zookeeperProperties.getZkTimeOut());
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
         checkOpenConnecton();
         return zkl.getStrPathData(path);
    }

    @Override
    public String getData(String path , AsyncCallback.DataCallback cb, Object ctx) throws ZKCliException {
        checkOpenConnecton();
        GetDataCallback callback = new GetDataCallback();
        CountDownLatch signal = new CountDownLatch(1);
        zkl.getZk().getData(path,false,callback,signal);
        return callback.getResult();
    }

    @Override
    public void setData(String path,String data) throws ZKCliException {
        PathExistCheck pathExistCheck = new PathExistCheck(zkl.getZk(),path,null);
        if(!pathExistCheck.exist()){
            zkl.createPathRecursively(path, CreateMode.PERSISTENT);
        }
        PathDataSetter pathDataSetter = new PathDataSetter(zkl.getZk(),path,null);
    }

    @Override
    public void setData(String path,String data, AsyncCallback.StatCallback cb, Object ctx) throws ZKCliException {
        checkOpenConnecton();
        SetDataCallback callback = new SetDataCallback();
        CountDownLatch signal = new CountDownLatch(1);
        zkl.getZk().setData(path,data.getBytes(),-1,callback,signal);
        try {
            signal.await(2, TimeUnit.SECONDS);
        }catch (Exception e){
            log.error(e);
        }
    }

    @Override
    public List<String> getChildren(String path) throws KeeperException, InterruptedException {
        return new PathChildrenGetter(zkl.getZk(),path).getChildren();
    }

    @Override
    public List<String> getChildren(String path, AsyncCallback.ChildrenCallback cb, Object ctz) throws ZKCliException {
        CountDownLatch sign = new CountDownLatch(1);
        GetChildrenCallback  childrenCallback = new GetChildrenCallback();
        zkl.getZk().getChildren(path,false,childrenCallback,sign);
        return childrenCallback.getResult();
    }

    @Override
    public void delete(String path) throws ZKCliException {
        new  PathDeleter(zkl.getZk(),path);
    }

    @Override
    public void delete(String path, boolean cascade, AsyncCallback.VoidCallback cb, Object ctx) throws ZKCliException {
        CountDownLatch sign = new CountDownLatch(1);
        SetVoidCallback voidCallback = new SetVoidCallback();
        zkl.getZk().delete(path,-1,voidCallback,sign);
    }

    @Override
    public void close() throws ZKCliException {
        try{
            zkl.getZk().close();
        }catch (InterruptedException e){
            log.error("close zk connection exception cause by:",e);
        }
    }


    class SetDataCallback implements AsyncCallback.StatCallback {
        @Override
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            if(code.equals(KeeperException.Code.OK)){

            }else if(code.equals(KeeperException.Code.SESSIONEXPIRED)){
                reconnect();
            }
        }
    }
    class GetDataCallback implements AsyncCallback.DataCallback{
        private String result;

        String getResult() {
            return result;
        }

        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            CountDownLatch countDownLatch = (CountDownLatch)ctx;
            if(code.equals(KeeperException.Code.OK)){
                if(data!=null){
                    result = new String(data);
                }
            }
            if(code.equals(KeeperException.Code.SESSIONEXPIRED)){
                reconnect();
            }
            countDownLatch.countDown();
        }
    }
    class GetChildrenCallback implements AsyncCallback.ChildrenCallback{
        private List<String> result;
        List<String> getResult() {
            return result;
        }

        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            KeeperException.Code code = KeeperException.Code.get(rc);
            CountDownLatch countDownLatch = (CountDownLatch)ctx;
            if(code.equals(KeeperException.Code.OK)){
                if(children != null && children.size()>0){
                    result = new ArrayList<String>(children);
                }
            }
            if(code.equals(KeeperException.Code.SESSIONEXPIRED)){
                reconnect();
            }
            countDownLatch.countDown();
        }
    }
    class SetVoidCallback implements AsyncCallback.VoidCallback{
        @Override
        public void processResult(int rc, String path, Object ctx) {
            CountDownLatch countDownLatch = (CountDownLatch)ctx;
            KeeperException.Code  code = KeeperException.Code.get(rc);
            if(code.equals(KeeperException.Code.SESSIONEXPIRED)){
                reconnect();
            }
        }
    }
}
