package com.zero.orzprofiler.profiler.Coordintor;

import org.junit.Test;

/**
 * User: luochao
 * Date: 13-11-8
 * Time: 涓嫔崃6:52
 */
public class FutureTaskTest {
    @Test
    public void FutureTaskExcute(){
       for(int i = 0;i<100;i++){
           LoadTask<String> task = LoadTask.factory("task1 created by luochao"+i);
           AsyTaskFuture<String> asyTask = new AsyTaskFuture<String>(task);
           Thread t = new Thread(asyTask);
           t.start();
           System.out.println("+++++++++++++"+asyTask.getResult());
       }
    }


}
