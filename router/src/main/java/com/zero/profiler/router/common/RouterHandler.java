package com.zero.profiler.router.common;

import com.zero.profiler.router.RouterException;
import com.zero.profiler.router.RouterService;
import com.zero.profiler.router.loadbalance.RouterContext;
import org.apache.thrift.TException;

import java.util.Map;

/**
 * User: luochao
 * Date: 13-12-3
 * Time: 下午2:37
 */
public class RouterHandler implements RouterService.Iface {
    private RouterContext routerContext;

    public RouterHandler(RouterContext routerContext) {
        this.routerContext = routerContext;
    }

    @Override
    public String getBroker(String user, String pwd, String topic, String apply, Map<String, String> prop) throws RouterException, TException {
        return null;
    }
}
