package com.zero.profiler.router.common;

import com.alibaba.fastjson.JSON;
import com.zero.profiler.router.exception.ServiceException;
import org.apache.log4j.Logger;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

/**
 * User: luochao
 * Date: 13-10-29
 * Time: 下午5:48
 */
public class Util {
    private final static Logger logger = Logger.getLogger(Util.class);
    private static Properties prop;

    public static Properties getConf() throws Exception{
        if(prop==null){
           prop = loadConf();
        }
        return prop;
    }
    public static String getStrParam(String paramName,String param,String defaultParam,boolean nullCheck){
        if(isNotBlank(param)){
            return param;
        }else{
            if(nullCheck && defaultParam == null){
                throw  new RuntimeException(String.format("The param[%s] needs a required value",paramName));
            }
            return  defaultParam;
        }
    }
    public static int getIntParam(String paramName,String param){
        return getIntParam(paramName,param,0,0,Integer.MAX_VALUE,false);
    }
    public static int getIntParam(String paramName,String param,int defaultParam,int start,int end){
        return getIntParam(paramName,param,defaultParam,start,end,false);
    }
    public static int getIntParam(String paramName,String param,int defaultParam,int start,int end,boolean nullcheck){
        if(param != null){
            int paramInt = Integer.parseInt(param);
            if(paramInt<start||paramInt>end){
              throw new RuntimeException(String.format("the param is not at the range [%s - %s]",start,end));
            }else {
                return paramInt;
            }
        }else{
             if(nullcheck){
                 throw  new RuntimeException(String.format("the param is required [%s]",paramName));
             }
             return defaultParam;
        }
    }
    private static Properties loadConf() throws ServiceException{
        Properties propEntity = new Properties();
        try{
            propEntity.load(Util.class.getClassLoader().getResourceAsStream(RouterConstants.ROUTER_PATH));
        }catch (FileNotFoundException e){
            throw new ServiceException(String.format("The router_path does not exist, [%s]",RouterConstants.ROUTER_PATH));
        }
        catch (IOException e){
            throw  new ServiceException(String.format("some error occur when read router_path ,[%s] cause by:",RouterConstants.ROUTER_PATH),e.getCause());
        }
        return propEntity;
    }
    public static boolean isNotBlank(String str){
        if(str == null){
           return false;
        }
        if(str.length()==0){
            return false;
        }
        return  true;
    }
    public static synchronized String getMD5(String s){
        String result = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(s.getBytes());
           // result = md5.digest();
        }catch (NoSuchAlgorithmException e){

        }
        return  result;
    }
    public static String nibber2char(byte[] bytes){


        return "";
    }
    public static  String getHostName(){
        String hostAddr = null;
        try{
            InetAddress address = InetAddress.getLocalHost();
            hostAddr = address.getHostName();
        }catch (UnknownHostException e){

        }

        return hostAddr;

    }
    public static String toJsonString(Object o){
        return JSON.toJSONString(o);
    }
    public static Object parseObject(String jsonStr,Class<?> cls){
        return JSON.parseObject(jsonStr,cls);
    }



}
