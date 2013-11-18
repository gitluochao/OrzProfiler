package com.zero.profiler.router.process;

import com.zero.profiler.router.RouterException;
import com.zero.profiler.router.RouterService;
import com.zero.profiler.router.loadbalance.RouterContext;
import org.apache.thrift.TException;

import java.util.Map;

/**
 * User: luochao
 * Date: 13-11-18
 * Time: 下午1:46
 */
public class BizProcess  implements RouterService.Iface {
    private RouterContext routerContext;

    public BizProcess(RouterContext routerContext) {
        this.routerContext = routerContext;
    }

    @Override
    public String getBroker(String user, String pwd, String topic, String apply, Map<String, String> prop) throws RouterException, TException {
        return null;
    }
}
