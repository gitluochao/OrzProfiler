package com.zero.profiler.router.service;

import com.zero.profiler.router.exception.ServiceException;
import com.zero.profiler.router.loadbalance.RouterContext;
import org.apache.log4j.Logger;

/**
 * User: luochao
 * Date: 13-11-18
 * Time: 上午11:23
 */
public class SampleServerEngine extends ServerEngine {
    private static final Logger log = Logger.getLogger(SampleServerEngine.class);
    private RouterContext context ;
    @Override
    public void doStart() throws ServiceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void doStop() throws ServiceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void doServer() throws ServiceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
