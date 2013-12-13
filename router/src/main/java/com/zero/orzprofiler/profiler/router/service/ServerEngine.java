package com.zero.orzprofiler.profiler.router.service;

import com.zero.orzprofiler.profiler.router.common.ParamKey;
import com.zero.orzprofiler.profiler.router.common.Util;
import com.zero.orzprofiler.profiler.router.exception.ServiceException;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: luochao
 * Date: 13-11-15
 * Time: 下午4:45
 */
public abstract class ServerEngine implements Server{
    public   ServerProperties serverProperties;
    /**
     *
     */
    private  final AtomicBoolean starting = new AtomicBoolean(false);
    private  final AtomicBoolean started = new AtomicBoolean(false);
    private  final AtomicBoolean stoping = new AtomicBoolean(false);
    private  final AtomicBoolean stoped = new AtomicBoolean(false);
    private  final AtomicBoolean shutdowning = new AtomicBoolean(false);
    private  final AtomicBoolean shutdown = new AtomicBoolean(false);

    /**
     * 初始化参数可以在抽象类执行
     */
    public void start() throws Exception{
        this.serverProperties = new ServerProperties(Util.getConf());
        if(!started.get()){
            if(starting.compareAndSet(false,true)){
                Exception ex = null;
                boolean childrenStarted = false;
                try{
                    doStart();
                }catch (ServiceException e){
                    ex  = e;
                }finally {
                    if(ex != null){
                       try{
                            stop(childrenStarted);
                       }catch (Exception e){

                       }
                       throw ex;
                    }
                    try {
                        started.set(true);
                        starting.set(false);
                        shutdown.set(false);
                        shutdowning.set(false);
                        stoping.set(false);
                        stoped.set(false);
                        doServer();
                    }catch (Exception e){
                         throw e;
                    }
                }
            }
        }

    }
    public void stop(boolean childrenStarted) throws Exception{
        if(stoping.compareAndSet(false,true)){
            try{
                if(childrenStarted){
                    doStop();
                }
            }catch (Exception e){

            }finally {
                stoping.set(false);
                stoped.set(false);
                starting.set(false);
                started.set(false);
                shutdowning.set(false);
                shutdown.set(false);
            }
        }
    }
    public void stop()  throws Exception{
        if(stoped.get()){
            doStop();
        }
    }

    public boolean isStarted() {
        return started.get();
    }

    public boolean isStoped() {
        return stoped.get();
    }

    public boolean isShutdown() {
        return shutdown.get();
    }

    public abstract void doStart() throws ServiceException;
    public abstract void doStop() throws ServiceException;
    public abstract void doServer() throws ServiceException;

    public static void main(String args[]){
        final Logger log = Logger.getLogger(ServerEngine.class);
        Thread.currentThread().setName("main server thread");
        try{
           Properties prop =  Util.getConf();
           String name = prop.getProperty(ParamKey.Server.serverType,"TREADPOOL");
           String className = ParamKey.Server.ServerClass.valueOf(name).getClassName();
           final  ServerEngine  engine = ServerEngineFactory.getInstance(className);
           //
           Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
               @Override
               public void run() {
                   try{
                        log.info("engine stop....");
                        engine.doStop();
                   }catch (Exception e){
                       log.error(e);
                       System.exit(-1);
                   }
               }
           }));
           try{
               engine.start();
           }catch (Exception e){
               log.error(e);
               System.exit(-1);
           }

        }catch (Exception e){
            log.error(e);
            System.exit(-1);
        }
    }
}
