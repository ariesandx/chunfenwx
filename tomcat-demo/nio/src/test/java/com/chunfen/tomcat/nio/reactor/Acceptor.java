package com.chunfen.tomcat.nio.reactor;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * 初始 reactor 关注 accept 事件，dispatch 会 调用 accept 的run 方法
 * 拿到 socketChannel 的时候 创建  socketHandler 让其 执行 连接完成后的  后续方法
 */
public class Acceptor implements Runnable {

    private Reactor reactor;

    public Acceptor(Reactor reactor){
        this.reactor=reactor;
    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel=reactor.getServerSocketChannel().accept();
            System.out.println(Thread.currentThread().getName() + " Acceptor accept a client");
            //调用Handler来处理channel
            if(socketChannel!=null){
                new SocketHandler(reactor.getSelector(), socketChannel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
