package com.zero.orzprofiler.profiler.Factory;

import com.zero.orzprofiler.profiler.router.zookeeper.ZookeeperExecute;
import com.zero.orzprofiler.profiler.router.zookeeper.ZookeeperPoolFactory;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * User: luochao
 * Date: 13-11-21
 * Time: 上午10:25
 */
public class TestZookeeper {
    @Test
    public void testFactory(){

       // zookeeperOperation.getChildren();
    }
    private static CountDownLatch latch = new CountDownLatch(1);
    public static void main(String args[]) {
        ZookeeperPoolFactory factory = ZookeeperPoolFactory.getInstance();
        ZookeeperExecute zookeeperOperation = factory.getZookeeperClient();
        try{
      //      ZooKeeper zk = new ZooKeeper("192.168.192.135/zookeeper",5000,new Watch());
            Stat stat = zookeeperOperation.getZooKeeper().exists("/www",false);
            System.out.println("attempt connect to zk");
            latch.await();
        }catch (Exception e){

        }

    }
    static class Watch implements Watcher {
        public void process(WatchedEvent event) {
            if(event.getState()== Event.KeeperState.SyncConnected){
                System.out.println("get out");
                latch.countDown();
            }
        }
    }
}
