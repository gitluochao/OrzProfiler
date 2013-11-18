package com.zero.profiler.router.zookeeper;

import com.zero.profiler.router.exception.ZKCliException;
import org.apache.zookeeper.AsyncCallback;

import java.util.List;

/**
 * User: luochao
 * Date: 13-11-15
 * Time: 下午3:58
 */
public class ZookeeperOperation extends  ZookeeperRecyclableService implements ZookeeperService {
    public ZookeeperOperation(ZookeeperProperties zookeeperProperties){
        super(zookeeperProperties.getServerList(),zookeeperProperties.getZkPoolSize());

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
