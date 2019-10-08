package com.chunfen.tomcat.nio.reactor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

@Getter
@Setter
public class Reactor implements Runnable{

    private ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public Reactor(int port){
        try {
            selector=Selector.open();
            serverSocketChannel=ServerSocketChannel.open();
            InetSocketAddress inetSocketAddress=new InetSocketAddress(InetAddress.getLocalHost(),port);
            serverSocketChannel.bind(inetSocketAddress);
            serverSocketChannel.configureBlocking(false);

            //向selector注册该channel
            SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            //利用selectionKey的attache功能绑定Acceptor 如果有事情，触发Acceptor
//            selectionKey.attach(new Acceptor(this));
            //使用线程池 的 acceptor
            selectionKey.attach(new PoolAcceptor(this));
            System.out.println(Thread.currentThread().getName() + " Reactor starting");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + " Reactor started");
            while(!Thread.interrupted()){
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                //Selector如果发现channel有OP_ACCEPT或READ事件发生，下列遍历就会进行。
                while(iterator.hasNext()){

                    //来一个事件 第一次触发一个accepter线程
                    //以后触发SocketReadHandler
                    SelectionKey selectionKey = iterator.next();
                    System.out.println(Thread.currentThread().getName() + " Reactor from client select" + objectMapper.writeValueAsString(selectionKey));

                    //所有的关心的 io 事件 都 通过 dispatch 进行分发
                    dispatch(selectionKey);
    //                selectionKeys.clear();
                    iterator.remove();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 运行Acceptor或SocketReadHandler
     * @param key
     */
    void dispatch(SelectionKey key) {
        // 不同的 key 触发不同的 事件
        // 注意 此处 attachment 不一定 必须是 Runnable 接口的实现，也可以是公共的接口
        Runnable r = (Runnable)(key.attachment());
        if (r != null){
            r.run();
        }
    }
}
