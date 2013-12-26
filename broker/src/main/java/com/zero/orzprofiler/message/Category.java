package com.zero.orzprofiler.message;

import java.util.Set;

/**
 * User: luochao
 * Date: 13-12-25
 * Time: 下午6:08
 */
public interface Category {
    /**
     * jurge is valid subscriber
     * @param key
     * @return
     */
    boolean isInvalidSubscriber(String key);

    /**
     * jurger message is expire
     * @param created
     * @return
     */
    boolean isMessageExpireAfter(long created);

    /**
     * jurge readers is exist in catagory
     * @param subscribers
     * @return
     */
    boolean isMessageUselessReadBy(Set<String> subscribers);

    String name();
}
