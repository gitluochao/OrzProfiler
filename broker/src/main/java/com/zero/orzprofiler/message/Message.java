package com.zero.orzprofiler.message;

import com.zero.orzprofiler.session.Session;
import com.zero.orzprofiler.zookeeper.Disposeable;

import java.util.Set;

/**
 * User: luochao
 * Date: 13-12-26
 * Time: 下午1:36
 */
public interface Message<MessageContent> extends Disposeable,Freezeable<MessageContent>{
    /**
     * message Category
     * @return
     */
    Category category();

    /**
     * message content
     * @return
     */
    MessageContent content();

    /**
     * jurge message is expire
     * @return
     */
    boolean isExpired();

    /**
     *jurger message is useless
     * @return
     */
    boolean isUseLess();

    /**
     * message create mill
     * @return
     */
    long created();

    /**
     * message pub
     * @return {@link Session}
     */
    Session publisher();

    /**
     * register subscriber
     * @param subscriber
     */
    void registerReader(Session subscriber);

    /**
     * return all {@link Message} subscriber
     * @return
     */
    Set<String> subscribers();


}
