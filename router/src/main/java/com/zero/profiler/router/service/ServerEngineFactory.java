package com.zero.profiler.router.service;

import com.zero.profiler.router.exception.ServiceException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: luochao
 * Date: 13-11-15
 * Time: 下午4:53
 */
public class ServerEngineFactory {
    private static final Map<String,ServerEngine> engines = new ConcurrentHashMap<String,ServerEngine>();
    public static  ServerEngine getInstance(String engineName) throws  ServiceException{
        ServerEngine engine = null;
         if(engines.get(engineName)!=null){
             engine = engines.get(engineName);
         }else{
             try {
                 engine =(ServerEngine) Class.forName(engineName).newInstance();

                 engines.put(engineName,engine);
             }catch (ClassNotFoundException e){
                 throw  new ServiceException("get engine  error cause by  class not found",e.getCause());
             }catch (InstantiationException e){
                 throw  new ServiceException("engine name error cause by  class can't InstantiationException",e.getCause());
             }catch (IllegalAccessException e){
                 throw  new ServiceException("engine name error cause by  class can't acess this class ",e.getCause());
             }
         }
         return engine;
    }
}
