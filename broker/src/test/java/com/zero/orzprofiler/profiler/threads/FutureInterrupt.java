package com.zero.orzprofiler.profiler.threads;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * User: luochao
 * Date: 13-11-13
 * Time: 下午8:20
 */
public class FutureInterrupt {
    interface CancelRunable<T> extends Callable<T>{
        public void cancel();
        public RunnableFuture<T> newTask();
    }
    class CancelExecutorPool extends ThreadPoolExecutor{
        CancelExecutorPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        @Override
        protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
            if(callable instanceof CancelRunable){
                return ((CancelRunable<T>)callable).newTask();
            }
            return super.newTaskFor(callable);

        }
        private <T>  List<T> get(T t){
            return new ArrayList<T>();
        }
    }
    abstract class SocketTask<T> implements CancelRunable<T>{
        private Socket socket;

        void setSocket(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void cancel() {
            try{
                if(socket != null){
                    socket.close();
                }
            }catch (Exception e){

            }
        }


        public RunnableFuture<T> newTask() {
             return new FutureTask<T>(this){
                 @Override
                 public boolean cancel(boolean mayInterruptIfRunning) {
                     try{
                        SocketTask.this.cancel();
                     }catch (Exception e){

                     }finally {
                         return super.cancel(mayInterruptIfRunning);
                     }
                 }
             };
        }
    }

}
