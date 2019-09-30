package com.chunfen.tomcat.nio.reactor;

import com.chunfen.tomcat.nio.Message;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;

/**
 *  创建 SocketHandler 的时候 会注册 对应的 读写事件，后续 如果有 事件发生， 会 由 reactor 调用 run方法
 *
 *  一个 socketHandler 对应 一个  selectionKey，也 对应 一组 （ sockelChannel 和 关注的 事件）
 *  当发生 selectionKey 对应的 事件 时，在 com.chunfen.tomcat.nio.reactor.Reactor#dispatch(java.nio.channels.SelectionKey)
 *  方法中 会调用run 方法，根据 channel 的 状态 进行 发送/接收数据
 */
public class SocketHandler implements Runnable{

    private SocketChannel socketChannel;
    private SelectionKey selectionKey;

    private ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);

    public SocketHandler(Selector selector, SocketChannel socketChannel) throws IOException {
        this.socketChannel=socketChannel;
        socketChannel.configureBlocking(false);

        this.selectionKey = this.socketChannel.register(selector, 0);

        //将SelectionKey绑定为本Handler 下一步有事件触发时，将调用本类的run方法。
        //参看dispatch(SelectionKey key)
        selectionKey.attach(this);

        //同时将SelectionKey标记为可读，以便读取。
        selectionKey.interestOps(SelectionKey.OP_READ);
        // 可以 立即wakeup  也可不调用
//        selector.wakeup();
    }

    /**
     * 处理读取数据
     */
    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + " SocketHandler running");
            if(selectionKey.isReadable()){
                read();
            } else if(selectionKey.isWritable()){
                send();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void read() throws IOException {
        input.clear();
        socketChannel.read(input);
        if (inputIsComplete()) {
            process();
            // Normally also do first write now
            selectionKey.interestOps(SelectionKey.OP_WRITE); //第三步,接收write事件
            // 可以 立即wakeup  也可不调用
//            selectionKey.selector().wakeup();
        }
    }
    void send() throws IOException {

        output.clear();
        Message<Date> dateMessage = new Message<Date>();
        dateMessage.setData(new Date());
        dateMessage.setCode(11);

        System.out.println(Thread.currentThread().getName() + " SocketHandler send message" + objectMapper.writeValueAsString(dateMessage));

        socketChannel.write(ByteBuffer.wrap(objectMapper.writeValueAsBytes(dateMessage)));
        //write完就结束了, 关闭select key
        if (outputIsComplete()) {
            selectionKey.cancel();
        }
    }

    private boolean inputIsComplete() {
        /* ... */
        return true;
    }

    private boolean outputIsComplete() {

        /* ... */
        return true;
    }

    private void process() throws IOException {
        /* ... */
        input.flip();
        byte[] bytes = new byte[input.remaining()];
        input.get(bytes);
//        Message obj = objectMapper.readValue(bytes, Message.class);
        System.out.println(Thread.currentThread().getName() + " SocketHandler read message"+new String(bytes, "utf-8"));
        return;
    }
}
