package com.zero.orzprofiler.util;

/**
 * User: luochao
 * Date: 13-12-27
 * Time: 上午10:26
 */
public class MemoryMonitor {
    private long shortage;
    private long abundant;

    public MemoryMonitor(long shortage, long abundant) {
        if (shortage > abundant || abundant > max())
            throw new IllegalStateException(String.format("illegal shortage:[%s] abundant:[%s]",shortage,abundant));
        this.shortage = shortage;
        this.abundant = abundant;
    }
    //free memory
    private long free(){
        return Runtime.getRuntime().freeMemory();
    }
    public boolean isShortage(){
       return free() <= shortage;
    }
    public boolean isAbundant(){
        return free() >= abundant;
    }
    //max memory
    private long max(){
        return Runtime.getRuntime().maxMemory();
    }
}
