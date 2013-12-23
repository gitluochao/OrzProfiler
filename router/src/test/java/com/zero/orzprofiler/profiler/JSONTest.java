package com.zero.orzprofiler.profiler;

import com.zero.orzprofiler.profiler.router.common.Util;
import com.zero.orzprofiler.profiler.shutdown.JavaBean;
import org.junit.Test;

/**
 * User: luochao
 * Date: 13-12-20
 * Time: 下午1:57
 */
public class JSONTest {
    @Test
    public void testGson(){
        String json = "{\"id\": \"1\",\"name\": \"luochao\"}";

        JavaBean javaBean =(JavaBean) Util.parseObject(json, JavaBean.class);
        System.out.println(javaBean);
        JavaBean javaBean1 = new JavaBean();
        javaBean.setId(100);
        javaBean.setName("luochao");
        String json2 = Util.toJsonString(javaBean);
        System.out.println(json2);
    }
}
