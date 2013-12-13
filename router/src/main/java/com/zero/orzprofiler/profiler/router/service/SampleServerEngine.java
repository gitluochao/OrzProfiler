package com.zero.orzprofiler.profiler.router.service;

import com.zero.orzprofiler.profiler.router.RouterService;
import com.zero.orzprofiler.profiler.router.exception.ServiceException;
import com.zero.orzprofiler.profiler.router.loadbalance.RouterContext;
import com.zero.orzprofiler.profiler.router.process.RouterHandler;
import org.apache.log4j.Logger;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * User: luochao
 * Date: 13-11-18
 * Time: 上午11:23
 */
public class SampleServerEngine extends ServerEngine {
    private static final Logger log = Logger.getLogger(SampleServerEngine.class);
    private RouterContext context ;
    private TServer tServer;
    @Override
    public void doStart() throws ServiceException {
        log.info("sample thrift server starting .................");
        context = RouterContext.getRouterContext();
        int port = this.serverProperties.getPort();
        String address = this.serverProperties.getBindadr();
        try{
            InetAddress inetAddress = InetAddress.getByName(this.serverProperties.getBindadr());
            InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress,port);
            TServerTransport serverTransport = new TServerSocket(inetSocketAddress,this.serverProperties.getCliTimeout());
            TProcessor processor = new RouterService.Processor(new RouterHandler(context));
            TServer.Args  args = new TServer.Args(serverTransport);
            args.processor(processor);
            tServer = new TSimpleServer(args);
            log.info("simple server bind port : "+this.serverProperties.getPort());
        }catch (Exception e){
           throw new ServiceException("there some fatal error in init simple server....",e);
        }
    }

    @Override
    public void doStop() throws ServiceException {
        log.info("the simple thrift service stoping .. .. ..  .. ");
        try{
            tServer.stop();
        }catch (Exception e){
            throw new ServiceException("there some fatal error accur in stop thrift server.....",e);
        }
    }

    @Override
    public void doServer() throws ServiceException {
        log.info("the simple thrift service servering  .. .. ..  .. ");
       try {
           tServer.serve();
       }catch (Exception e){
           throw new ServiceException("the some fatal error accur in open server .....",e);
       }
    }
}
