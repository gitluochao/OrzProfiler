package com.zero.orzprofiler.swap;

import java.lang.ref.Reference;
import java.nio.ByteBuffer;
import com.zero.orzprofiler.swap.Buffer.Slice;
/**
 * User: luochao
 * Date: 13-12-20
 * Time: 上午10:55
 */
public class ByteBufferPoint implements Point<ByteBuffer> {
    private Reference<ByteBuffer> reference;
    private Slice slice;

    private boolean removed = false;

    public ByteBufferPoint(Reference<ByteBuffer> reference , Slice slice) {
        this.slice = slice;
        this.reference = reference;
    }

    @Override
    public synchronized ByteBuffer get() {
        if (removed) throw new IllegalStateException("can not get after point dispost");
        ByteBuffer result = reference.get();
        if(result == null){
            result = slice.get();
        }
        return result;
    }

    @Override
    public synchronized void dispose() {
        if(removed) return;
        if (reference.get() != null)
            reference.clear();
        slice.remove();
        removed = true;
    }
}
