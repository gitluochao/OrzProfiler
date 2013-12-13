package com.zero.orzprofiler.profiler.router.zookeeper;

import java.util.List;

/**
 * User: luochao
 * Date: 13-10-30
 * Time: 下午4:09
 */
public interface Visitor {
    void onNodeChildrenChanged(String path, List<String> children);

    void onNodeCreated(String path);

    void onNodeDataChanged(String path);

    void onNodeDeleted(String path);
}
