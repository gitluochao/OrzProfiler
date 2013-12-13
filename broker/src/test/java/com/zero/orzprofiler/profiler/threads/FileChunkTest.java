package com.zero.orzprofiler.profiler.threads;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * User: luochao
 * Date: 13-12-7
 * Time: 上午11:19
 */
public class FileChunkTest {
    @Test
    public void testFileCreate() throws IOException {
        File file = new File("E:\\export","luochao");
        file.createNewFile();
    }
}
