package com.chunfen.tomcat.nio.reactor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ReadHandler implements Runnable {

    private SocketChannel socketChannel;
    private SelectionKey selectionKey;

    private ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    ByteBuffer input = ByteBuffer.allocate(1024);

    public ReadHandler(Selector selector, SocketChannel socketChannel)  throws IOException {
        this.socketChannel=socketChannel;
        this.socketChannel.configureBlocking(false);

        //将SelectionKey绑定为本Handler 下一步有事件触发时，将调用本类的run方法。
        // 一个坑 如果 selector 优先 被select() 方法阻塞， 要优先调用 wakeup()方法，再进行 register 的动作
        selector.wakeup();
        this.selectionKey = this.socketChannel.register(selector, SelectionKey.OP_READ);
        selectionKey.attach(this);
    }

    @Override
    public void run() {
        //        方式二  使用状态模式（State-Object Pattern）
//        selectionKey 是 context 环境  attachment 就是  state
//        更换 attachment  reactor 的 dispatch 方法 会执行不同的回调
        try {
            socketChannel.read(input);
            if (inputIsComplete()) {
                process();
                selectionKey.attach(new SendHandler(socketChannel, selectionKey));  //状态迁移, Read后变成write, 用Sender作为新的callback对象
                selectionKey.interestOps(SelectionKey.OP_WRITE);
                selectionKey.selector().wakeup();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean inputIsComplete() {
        /* ... */
        return true;
    }

    private void process() throws IOException {
        /* ... */
        input.flip();
        byte[] bytes = new byte[input.remaining()];
        input.get(bytes);
//        Message obj = objectMapper.readValue(bytes, Message.class);
        System.out.println(Thread.currentThread().getName() + " ReadHandler read message"+new String(bytes, "utf-8"));
        return;
    }
}
