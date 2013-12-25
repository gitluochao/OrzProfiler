package com.zero.orzprofiler.util;

import java.util.concurrent.ExecutionException;

/**
 * User: luochao
 * Date: 13-12-25
 * Time: 下午6:11
 */
public interface IRepository<K,V>  {
   V uncheckedGetOrCreateIfNoExist(final K key) throws InterruptedException, ExecutionException;

   V getOrCreateIfNoExist(final K key) throws  InterruptedException , ExecutionException;
}
