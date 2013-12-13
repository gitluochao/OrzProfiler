package com.zero.orzprofiler.profiler.threads;

/**
 * User: luochao
 * Date: 13-11-15
 * Time: 上午10:20
 */
public class Adapter extends Source implements TargetPattern {
    @Override
    public void method2() {
        System.out.println("method 2");
    }
}
