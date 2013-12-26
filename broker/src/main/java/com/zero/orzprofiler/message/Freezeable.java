package com.zero.orzprofiler.message;

import com.zero.orzprofiler.swap.Freezer;

/**
 * User: luochao
 * Date: 13-12-26
 * Time: 上午10:20
 */
public interface Freezeable<T> {
    /**
     * freeze by {@link Freezer}
     * @param freezer
     */
    void FreezeBy(Freezer<T> freezer);
}
