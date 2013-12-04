package com.zero.profiler.router.common;

import com.zero.profiler.router.exception.ServiceException;

import java.util.Properties;

/**
 * User: luochao
 * Date: 13-10-29
 * Time: 下午5:58
 */
public class RouterConstants {
    public static final String ROUTER_PATH = "router.properties";
    public static final String DEFAULT_SEVER_TYPE = "TREADPOOL";
    public static final String ID_SPILT = "_";
    public static final String LB_ROUND_ROBIN = "lb_round_robin";
    public static final String LB_RANDOM = "lb_random";
    public static final String LB_ROUND_ROBIN_STATELESS = "lb_round_robin_stateless";
    public static final String TIME_OUT = "time_out";
    public static final String RECEIVE_WINDOW_SIZE = "receive_window_size";


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
