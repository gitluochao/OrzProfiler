package com.zero.profiler.router.zookeeper;

import java.util.List;

/**
 * User: luochao
 * Date: 13-10-30
 * Time: 涓嫔崃4:09
 */
public interface Visitor {
    void onNodeChildrenChanged(String path, List<String> children);

    void onNodeCreated(String path);

    void onNodeDataChanged(String path);

    void onNodeDeleted(String path);
}
