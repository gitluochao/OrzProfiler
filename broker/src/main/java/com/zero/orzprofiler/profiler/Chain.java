package com.zero.orzprofiler.profiler;

import com.zero.orzprofiler.message.Message;
import com.zero.orzprofiler.zookeeper.Disposeable;
import com.zero.orzprofiler.zookeeper.Dumpable;

/**
 * Created by luochao on 14-1-5.
 */
public interface Chain<Content> extends Disposeable,Dumpable<Content> {
    Cursor<Content> cursorOf(Object key);

    void post(Message<Content> message);

    int size();

    void trim();

}
