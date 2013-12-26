package com.zero.orzprofiler.message;

import com.zero.orzprofiler.session.Session;

/**
 * User: luochao
 * Date: 13-12-26
 * Time: 下午2:53
 */
public interface MessageFactory<MessageContent> {
    Message<MessageContent> createBy(MessageContent content,Category category,Session session);
}
