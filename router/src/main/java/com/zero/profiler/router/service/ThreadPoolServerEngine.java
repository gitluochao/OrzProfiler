package com.zero.profiler.router.service;

import com.zero.profiler.router.exception.ServiceException;
import com.zero.profiler.router.loadbalance.RouterContext;
import org.apache.log4j.Logger;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * User: luochao
 * Date: 13-11-18
 * Time: 上午11:24
 */
public class ThreadPoolServerEngine extends ServerEngine {
    private static final Logger log = Logger.getLogger(SampleServerEngine.class);
    private RouterContext context ;
    private TThreadPoolServer tThreadPoolServer;
    @Override
    public void doServer() throws ServiceException {
        try{
            InetAddress address = InetAddress.getByName(serverProperties.getBindadr());
            InetSocketAddress socketAddress  = new InetSocketAddress(address,serverProperties.getPort());
            TServerTransport tServerTransport = new TServerSocket(socketAddress,serverProperties.getCliTimeout());
        }catch (Exception e){

        }
    }

    @Override
    public void doStop() throws ServiceException {
    }

    @Override
    public void doStart() throws ServiceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
