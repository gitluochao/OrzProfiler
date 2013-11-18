package com.zero.profiler.cache;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * User: luochao
 * Date: 13-11-9
 * Time: 涓嫔崃12:58
 */
public class CacheManagerTest {
    private final CountDownLatch startGate  = new CountDownLatch(1);
    private final CountDownLatch endGate = new CountDownLatch(10);
    private CacheManager<String,String> manager = new CacheManager<String, String>();
   @Test
    public void testCache(){
       for(int i=0;i<1;i++){
           Thread t = new Thread(){
               @Override
               public void run() {
                   try{
                       startGate.await();
                       for (int i = 0;i<100;i++){
                            manager.get("name");
                       }
                   }catch (InterruptedException e){
                       Thread.interrupted();
                   } finally {
                       endGate.countDown();
                   }
               }
           };
           t.start();
       }
       startGate.countDown();
       System.out.println("getCacheEnd.....");


   }
}
