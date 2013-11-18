package com.zero.profiler.threads;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

/**
 * User: luochao
 * Date: 13-11-12
 * Time: 下午2:06
 */
public class PrimeGen implements  Runnable{
    private  static final PrimeGen primeGen = new PrimeGen();
    private final List<BigInteger> result = new ArrayList<BigInteger>();
    private BlockingQueue queue = new ArrayBlockingQueue(4);
    private volatile  boolean cancled ;
    private PrimeGen(){

    }
    public static PrimeGen getInstance(){
        return primeGen;
    }
    private void cancled(){
        cancled = true;
    }
    @Override
    public void run() {
        BigInteger p = BigInteger.ONE;
        //use interrupte to termination thread
        while (!Thread.currentThread().isInterrupted() ){
            result.add(p);
        }
    }
    public synchronized List<BigInteger> get(){
        return new ArrayList<BigInteger>(result);
    }
    private void getValue(){
        //传递中断 阻塞线程中断 切换其他线程
        //queue.take();
    }

    public static void main(String[] args) {
        PrimeGen gen = PrimeGen.getInstance();
        Thread thread = new Thread(gen);
        thread.start();
        try{
            Thread.sleep(10);
        }catch (Exception e){

        } finally{
            gen.cancled();
            Thread.currentThread().interrupt();
            System.out.println("");
        }
    }
}
