package com.chunfen.tomcat.nio;

import com.chunfen.tomcat.nio.BaseTest;
import com.chunfen.tomcat.nio.Message;
import com.chunfen.tomcat.nio.reactor.Reactor;
import org.junit.Test;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ReactorTest extends BaseTest {

    @Test
    public void reactorTest() throws Exception{

        new Thread(new Reactor(8889)).start();
        System.out.println(Thread.currentThread().getName() + " ReactorTest reactor starting");
        Thread.sleep(300);

        try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(InetAddress.getLocalHost(), 8889))){
            System.out.println(Thread.currentThread().getName() + " ReactorTest client connected");
            socketChannel.write(Charset.forName("utf-8").encode("Hi~ server have a nice 假日"));

            ByteBuffer input = ByteBuffer.allocate(1024);
            socketChannel.read(input);

            input.flip();
            byte[] bytes = new byte[input.remaining()];
            input.get(bytes);

            Message obj = objectMapper.readValue(bytes, Message.class);
            System.out.println(Thread.currentThread().getName() + " " + obj.toString());

            // 维持  main
            Thread.sleep(20 * 1000);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
