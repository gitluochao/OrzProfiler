package org.apache.zookeeper.recipes;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * User: luochao
 * Date: 13-11-22
 * Time: 下午3:25
 */
public class PathVistor {
    private List<ACL> acls;
    /**
     * create Callback
     */
    private AsyncCallback.StringCallback stringCallback;
    private AsyncCallback.VoidCallback voidCallback;
    private AsyncCallback.StatCallback statCallback;
    private AsyncCallback.DataCallback dataCallback;
    private Stat stat;
    private Object ctx;
    private byte[] data;
    private Watcher watcher;

    public PathVistor(List<ACL> acls, AsyncCallback.StringCallback stringCallback, Object ctx, byte[] data) {
        this.acls = acls;
        this.stringCallback = stringCallback;
        this.ctx = ctx;
        this.data = data;
    }

    public PathVistor(AsyncCallback.VoidCallback voidCallback, Object ctx) {
        this.voidCallback = voidCallback;
        this.ctx = ctx;
    }

    public PathVistor(AsyncCallback.StatCallback statCallback, Object ctx, Watcher watcher) {
        this.statCallback = statCallback;
        this.ctx = ctx;
        this.watcher = watcher;
    }

    public PathVistor(AsyncCallback.DataCallback dataCallback, Stat stat, Watcher watcher) {
        this.dataCallback = dataCallback;
        this.stat = stat;
        this.watcher = watcher;
    }

    public PathVistor(byte[] data) {
        this.data = data;
    }

    public List<ACL> getAcls() {
        return acls;
    }

    public AsyncCallback.StringCallback getStringCallback() {
        return stringCallback;
    }

    public AsyncCallback.VoidCallback getVoidCallback() {
        return voidCallback;
    }

    public Object getCtx() {
        return ctx;
    }

    public byte[] getData() {
        return data;
    }

    public AsyncCallback.StatCallback getStatCallback() {
        return statCallback;
    }

    public Watcher getWatcher() {
        return watcher;
    }

    public AsyncCallback.DataCallback getDataCallback() {
        return dataCallback;
    }

    public Stat getStat() {
        return stat;
    }
}
