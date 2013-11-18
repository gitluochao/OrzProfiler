package com.zero.profiler.threads;

import java.util.concurrent.*;

/**
 * User: luochao
 * Date: 13-11-13
 * Time: 下午5:52
 */
public class ThrowableInterrupt {
   private static final ScheduledExecutorService exec =  new ScheduledThreadPoolExecutor(10);
   private static final Executor excutor = Executors.newFixedThreadPool(10);

   public static void  taskExcute(final Runnable r,final int timeout,final TimeUnit unit) throws InterruptedException{
        class ThreadRun implements Runnable{
             private volatile Throwable t;
             @Override
             public void run() {
                 try{
                     r.run();
                 }catch (Throwable e){
                     this.t = t;
                 }
             }
            void rethrow(){
                if(t!=null){
                }
            }
          }
       ThreadRun task = new ThreadRun();
       //利用线程中断来实现线程通信，以及关闭线程
       final Thread thread = new Thread(task);

       thread.start();
       exec.schedule(new Runnable() {
           @Override
           public void run() {
               thread.interrupt();
           }
       },timeout,unit);
       //等待线程中断 可能是出现异常中断 也可能是出现线程执行完成中断 缺点是无法判读两种情况 timeout
       thread.join(unit.toMillis(timeout));
       //经过timeout的时间如果进程还未中断 重新发出中断信号 告知其他线程
       task.rethrow();
       excutor.execute(task);

    }


}
