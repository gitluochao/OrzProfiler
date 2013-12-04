package com.zero.profiler.router.service;

import com.zero.profiler.router.RouterService;
import com.zero.profiler.router.exception.ServiceException;
import com.zero.profiler.router.loadbalance.RouterContext;
import com.zero.profiler.router.process.RouterHandler;
import org.apache.log4j.Logger;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * template method pattern
 * User: luochao
 * Date: 13-11-18
 * Time: 上午11:24
 */
public class NonBlockServerEngine extends ServerEngine {
    private static final Logger log = Logger.getLogger(NonBlockServerEngine.class);
    private RouterContext context ;
    private TServer nonBlockServer;

    @Override
    public void doStart() throws ServiceException {
        log.info("nonBlockServer thrift server starting .................");
        try{
               context = RouterContext.getRouterContext();
               InetAddress address = InetAddress.getByName(serverProperties.getBindadr());
               InetSocketAddress socketAddress  = new InetSocketAddress(address,serverProperties.getPort());
               TNonblockingServerTransport tServerTransport = new TNonblockingServerSocket(socketAddress,serverProperties.getCliTimeout());
               TProcessor processor = new RouterService.Processor(new RouterHandler(context));
               TProtocolFactory protocolFactory = new TBinaryProtocol.Factory();
               TNonblockingServer.Args args = new TNonblockingServer.Args(tServerTransport);
               args.processor(processor);
               args.maxReadBufferBytes = this.serverProperties.getMaxReadBufferBytes();
               TFramedTransport.Factory outputTransportFactory = new TFramedTransport.Factory();
               TProtocolFactory inputProtocolFactory = new TBinaryProtocol.Factory();
               TProtocolFactory outputProtocolFactory = new TBinaryProtocol.Factory();
               args.inputTransportFactory(outputTransportFactory);
               args.outputProtocolFactory(outputProtocolFactory);
               args.inputProtocolFactory(inputProtocolFactory);
               args.outputTransportFactory(outputTransportFactory);
               nonBlockServer = new TNonblockingServer(args);
               log.info("nonblock server bind port : "+this.serverProperties.getPort());
           }catch (Exception e){
               throw new ServiceException("there some fatal error in init threadpool server...bind port.",e);
           }
    }

    @Override
    public void doStop() throws ServiceException {
        log.info("the nonBlockServer thrift service stoping .. .. ..  .. ");
          try{
              nonBlockServer.stop();
          }catch (Exception e){
              throw new ServiceException("there some fatal error accur in stop thrift server.....",e);
          }
    }

    @Override
    public void doServer() throws ServiceException {
       log.info("the nonBlockServer thrift service servering  .. .. ..  .. ");
       try {
           nonBlockServer.serve();
       }catch (Exception e){
           throw new ServiceException("the some fatal error accur in open server .....",e);
       }
    }
}
