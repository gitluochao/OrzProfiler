package com.zero.profiler.Coordintor;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * User: luochao
 * Date: 13-11-8
 * Time: 涓嫔崃6:37
 */
public class CountDownTest {
    @Test
    public void testMutiThreadConcurrentExcute() throws Exception{
         final CountDownLatch startGate = new CountDownLatch(1);
         final CountDownLatch endGate = new CountDownLatch(10);
         for(int i=0;i<10;i++){
             Thread t = new Thread(){
                 @Override
                 public void run() {
                     try{
                        startGate.await();
                         System.out.println("excute");
                     }catch (Exception e){

                     }finally {
                         endGate.countDown();
                     }
                 }
             };
             t.start();
         }
        long start = System.currentTimeMillis();
        System.out.println("starttime"+start);
        startGate.countDown();
        endGate.await();
        System.out.println("endTime"+(System.nanoTime()-start));


    }
}
