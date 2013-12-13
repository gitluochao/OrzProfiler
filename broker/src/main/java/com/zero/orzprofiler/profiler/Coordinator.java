package com.zero.orzprofiler.profiler;

/**
 * User: luochao
 * Date: 13-10-25
 * Time: 4:01
 */
public interface Coordinator {
    /**
     * @param name
     * @return a new {@link Track} with name
     */
    Track track(String name);


    public  interface  Track{
        void destory();
        boolean move();
    }
}
