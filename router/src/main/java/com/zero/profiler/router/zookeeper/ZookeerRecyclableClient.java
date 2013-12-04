package com.zero.profiler.router.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.recipes.PathCreator;
import org.apache.zookeeper.recipes.PathDataGetter;


import java.util.HashMap;
import java.util.Map;

/**
 * User: luochao
 * Date: 13-10-29
 * Time: 下午5.36
 */
public class ZookeerRecyclableClient {
    private ZooKeeper zk;
    private Map<Thread,String> watchPath = new HashMap<Thread, String>();
    private Map<Thread,Visitor> visitors = new HashMap<Thread, Visitor>();
    //decorator patterner
    public ZookeerRecyclableClient(ZooKeeper zk) {
        this.zk = zk;
    }

    public ZooKeeper getZk() {
        return zk;
    }
    public void reconnect(ZooKeeper zooKeeper){
        try{
            getZk().close();
            this.zk = zooKeeper;
            if(watchPath != null){
                for(Map.Entry<Thread,String> entry:watchPath.entrySet()){
                    Thread watchThread = entry.getKey();
                    String path = entry.getValue();
                    if(watchThread!=null){
                        Visitor vistor = visitors.get(watchThread);
                        String threadName = watchThread.getName();
                        watchThread.interrupt();
                        if(threadName.indexOf("watchPathExist") > -1){
                            watchPathExist(path,vistor);
                        }else {
                            watchPathChild(path,vistor);
                        }
                    }

                }
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }

    }
    public void watchPathExist(final String path,final Visitor visitor){
        Thread watchThread = new Thread(){
            @Override
            public void run() {
                 new PathExistWatcher(zk,path,visitor).watcher();
            }
        };
        watchThread.setName("watchPathExist"+path);
        watchPath.put(watchThread,path);
        visitors.put(watchThread,visitor);
        watchThread.start();
    }
    public void watchPathChild(final String path,final  Visitor visitor){
        Thread watchThread = new Thread(){
            @Override
            public void run() {
                new PathChildWatcher(path,zk,visitor).watcher();
            }
        };
        watchThread.setName("watchPathChild"+path);
        watchPath.put(watchThread,path);
        visitors.put(watchThread,visitor);
        watchThread.start();
    }
    public void createPathRecursively(String path,CreateMode createMode){
        String[] pathSplit = path.split("/");
        String pathAbsolute = "";
        for(String pathStr:pathSplit){
            if("".equals(pathStr)){
                continue;
            }
            pathAbsolute += "/";
            pathAbsolute += pathStr;
            PathCreator pathCreator = new PathCreator(getZk(),path,createMode,null);
            pathCreator.create();
        }
    }
    public String getStrPathData(String path){
        byte[] data = new PathDataGetter(zk,path).getData();
        if(data!=null){
            return new String(data);
        }
        return null;
    }


}
