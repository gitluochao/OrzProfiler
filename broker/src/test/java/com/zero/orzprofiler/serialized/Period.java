package com.zero.orzprofiler.serialized;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: luochao
 * Date: 13-12-14
 * Time: 下午4:55
 * To change this template use File | Settings | File Templates.
 */
public class Period implements Serializable{
    private final Date start;
    private final Date end;

    public Period(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
        if(this.start.compareTo(this.end)>0){
            throw new IllegalArgumentException("strart"+"after end");
        }
    }
    public Date getStart(){
        return new Date(this.start.getTime());
    }
    public Date getEnd(){
        return new Date(this.end.getTime());
    }
    private void readObject(ObjectInputStream s) throws IOException , ClassNotFoundException{
        s.defaultReadObject();
        if (start.compareTo(end)>0){
            throw new IllegalArgumentException("strart"+"after end");
        }
    }

}
