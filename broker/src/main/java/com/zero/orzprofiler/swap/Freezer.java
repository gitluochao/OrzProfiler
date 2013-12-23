package com.zero.orzprofiler.swap;

import com.zero.orzprofiler.zookeeper.Disposeable;

/**
 * User: luochao
 * Date: 13-12-23
 * Time: 下午7:10
 */
public interface Freezer<T> extends Disposeable{
    Point<T> freeze(T message);
}
