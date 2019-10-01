package com.chunfen.tomcat.nio.reactor;

import com.chunfen.tomcat.nio.Message;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class SendHandler implements Runnable {

    private SocketChannel socketChannel;
    private SelectionKey selectionKey;
    ByteBuffer output = ByteBuffer.allocate(1024);

    private ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public SendHandler(SocketChannel socketChannel, SelectionKey selectionKey) {
        this.socketChannel = socketChannel;
        this.selectionKey = selectionKey;
    }

    @Override
    public void run() {
        try {
//            output.clear();
            Message<Date> dateMessage = new Message<Date>();
            dateMessage.setData(new Date());
            dateMessage.setCode(11);

            System.out.println(Thread.currentThread().getName() + " SocketHandler send message" + objectMapper.writeValueAsString(dateMessage));

            socketChannel.write(ByteBuffer.wrap(objectMapper.writeValueAsBytes(dateMessage)));
            //write完就结束了, 关闭select key
            if (outputIsComplete()) {
                selectionKey.cancel();
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }


    private boolean outputIsComplete() {

        /* ... */
        return true;
    }

}
