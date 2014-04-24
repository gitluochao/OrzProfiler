package com.zero.orzprofiler.zookeeper;

import com.zero.orzprofiler.message.Category;
import com.zero.orzprofiler.session.Session;

/**
 * Created by luochao on 14-1-12.
 */
public interface Appendable<Content> {
    void append(Category category,Session session,Content content);
}
