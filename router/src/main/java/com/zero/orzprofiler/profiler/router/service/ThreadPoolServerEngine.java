package com.zero.orzprofiler.profiler.router.service;

import com.zero.orzprofiler.profiler.router.RouterService;
import com.zero.orzprofiler.profiler.router.exception.ServiceException;
import com.zero.orzprofiler.profiler.router.loadbalance.RouterContext;
import com.zero.orzprofiler.profiler.router.process.RouterHandler;
import org.apache.log4j.Logger;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
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
    private TServer tThreadPoolServer;
    @Override
    public void doStart() throws ServiceException {
        try{
            context = RouterContext.getRouterContext();
            InetAddress address = InetAddress.getByName(serverProperties.getBindadr());
            InetSocketAddress socketAddress  = new InetSocketAddress(address,serverProperties.getPort());
            TServerTransport tServerTransport = new TServerSocket(socketAddress,serverProperties.getCliTimeout());
            TProcessor processor = new RouterService.Processor(new RouterHandler(context));
            TProtocolFactory protocolFactory = new TBinaryProtocol.Factory();
            TThreadPoolServer.Args args = new TThreadPoolServer.Args(tServerTransport);
            args.processor(processor);
            args.protocolFactory(protocolFactory);
            tThreadPoolServer = new TThreadPoolServer(args);
            log.info("tThreadPoolServer server bind port : "+this.serverProperties.getPort());
        }catch (Exception e){
            throw new ServiceException("there some fatal error in init threadpool server....",e);
        }
    }

    @Override
    public void doStop() throws ServiceException {
        log.info("the tThreadPoolServer thrift service stoping .. .. ..  .. ");
        try{
            tThreadPoolServer.stop();
        }catch (Exception e){
            throw new ServiceException("there some fatal error accur in stop tThreadPoolServer thrift server.....",e);
        }
    }

    @Override
    public void doServer() throws ServiceException {
       log.info("the tThreadPoolServer thrift service servering  .. .. ..  .. ");
       try {
           tThreadPoolServer.serve();
       }catch (Exception e){
           throw new ServiceException("the some fatal error accur in open  tThreadPoolServer server .....",e);
       }
    }
}
