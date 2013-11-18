package com.zero.profiler.router.loadbalance;

import com.zero.profiler.router.zookeeper.Visitor;

import java.util.List;

/**
 * User: luochao
 * Date: 13-11-1
 * Time: 下午6:02
 */
public class RouterContext implements Context,Visitor{
    public RouterContext() {
        init();
    }


    //懒工厂
   private static class Holder{
       static  final RouterContext routerContext = new RouterContext();
   }
   public static RouterContext getRouterContext(){
       return Holder.routerContext;
   }
   void init(){


   }


    @Override
    public void syn() {

    }

    @Override
    public void clean() {

    }

    @Override
    public void onNodeChildrenChanged(String path, List<String> children) {

    }

    @Override
    public void onNodeCreated(String path) {

    }

    @Override
    public void onNodeDataChanged(String path) {

    }

    @Override
    public void onNodeDeleted(String path) {

    }
}
