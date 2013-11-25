package com.zero.profiler.shutdown;



import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * User: luochao
 * Date: 13-11-18
 * Time: 下午2:53
 */
public class JavaHook {
    public static void main(String[] args) throws Exception{
        System.out.println("starting .................");
        Runtime.getRuntime().addShutdownHook(new Thread(new hookTask()));
        System.out.println("stoped");
        File file = new File("D:\\export\\home\\tomcat\\logs\\card.jd.com\\card-all.log");
        FileInputStream in = new FileInputStream(file);
        // java i/o decoration pattern
        BufferedInputStream bin = new BufferedInputStream(in);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        ReadableByteChannel read = Channels.newChannel(in);
        ByteArrayOutputStream output = new ByteArrayOutputStream(32*1024*16);
        WritableByteChannel write = Channels.newChannel(output);
        while (read.read(buffer)>0||buffer.position()!=0){
          //  write.close();
            buffer.flip(); //limit set as position ,position set to 0
        //    write.write(buffer);
            System.out.println(buffer.position());
        }
        //System.out.println(output.toByteArray());
        //perfect very exciting
        File baidu = new File("D:\\export\\home\\tomcat\\logs\\card.jd.com\\baidu.com.log");
        FileOutputStream out = new FileOutputStream(baidu);
        FileChannel channel = out.getChannel();
        URL url = new URL("http://wan.jd.com/");
        InputStream input =  url.openStream();
        ReadableByteChannel readChannel = Channels.newChannel(input);
        channel.transferFrom(readChannel,0,Integer.MAX_VALUE);

    }
   static   class  hookTask implements Runnable{
        @Override
        public void run() {
            try{
                Thread.sleep(100);
            }catch (Exception e){

            }finally {
                System.out.println("hook closed");
            }
        }
    }
}
