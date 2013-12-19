package com.zero.orzprofiler.zookeeper;

import com.zero.orzprofiler.message.AppendMessage;

/**
 * Created with IntelliJ IDEA.
 * User: luochao
 * Date: 13-12-16
 * Time: 下午9:33
 * To change this template use File | Settings | File Templates.
 */
public interface Dumpable<Message> {
    void dumpto(AppendMessage<Message> appendMessage);
}
