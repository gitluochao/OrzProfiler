package com.zero.profiler.router.zookeeper;

import com.zero.profiler.router.exception.ZKCliException;
import org.apache.zookeeper.AsyncCallback;

import java.util.List;

/**
 * User: luochao
 * Date: 13-10-29
 * Time: 上午6:36
 */
public interface ZookeeperService {
    public String getData(String path) throws ZKCliException;

    public String getData(String path,AsyncCallback.DataCallback cb,Object ctx) throws  ZKCliException;

    public void   setData(String path,String data) throws ZKCliException;

    public void   setData(String path,String data,AsyncCallback.StatCallback cb,Object ctx) throws ZKCliException;

    public List<String> getChildren(String path) throws  ZKCliException;

    public List<String> getChildren(String path,AsyncCallback.ChildrenCallback cb,Object ctz) throws  ZKCliException;

    public void delete(String path)throws  ZKCliException;

    public void delete(String path,boolean cascade,AsyncCallback.VoidCallback cb,Object ctx) throws  ZKCliException;

    public void close() throws ZKCliException;

}
