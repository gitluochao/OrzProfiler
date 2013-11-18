package com.zero.profiler.router.service;

import com.zero.profiler.router.exception.ServiceException;
import com.zero.profiler.router.loadbalance.RouterContext;
import org.apache.log4j.Logger;

/**
 * User: luochao
 * Date: 13-11-18
 * Time: 上午11:24
 */
public class NonBlockServerEngine extends ServerEngine {
    private static final Logger log = Logger.getLogger(NonBlockServerEngine.class);
    private RouterContext context ;

    @Override
    public void doStart() throws ServiceException {

    }

    @Override
    public void doStop() throws ServiceException {

    }

    @Override
    public void doServer() throws ServiceException {
    }
}
