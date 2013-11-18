package com.zero.profiler.router.common;

import com.zero.profiler.router.exception.ServiceException;

import java.util.Properties;

/**
 * User: luochao
 * Date: 13-10-29
 * Time: 涓嫔崃5:58
 */
public class RouterConstants {
    public  static final String ROUTER_PATH = "router.properties";


    public static void main(String[] args) throws ServiceException{
        Properties prop = new Properties();
        try {
            prop.load(RouterConstants.class.getClassLoader().getResourceAsStream(ROUTER_PATH));
            System.out.println("zkSize:"+prop.get(ParamKey.ZKClient.zkClientSize)+"zkHosts:"+prop.get(ParamKey.ZKService.hosts));
        }catch (Exception e){
            throw new ServiceException("read file error",e.getCause());
        }
    }
}
