package com.chunfen.tomcat.nio.reactor;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * 初始 reactor 关注 accept 事件，dispatch 会 调用 accept 的run 方法
 * 拿到 socketChannel 的时候 创建  socketHandler 让其 执行 连接完成后的  后续方法
 */
public class MainSubAcceptor implements Runnable {

    private MainReactor reactor;

    private SubReactor subReactor;

    public MainSubAcceptor(MainReactor reactor, SubReactor subReactor){
        this.reactor=reactor;
        this.subReactor = subReactor;
    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel= reactor.getServerSocketChannel().accept();
            System.out.println(Thread.currentThread().getName() + " MainSubAcceptor accept a client");
            //调用Handler来处理channel
            if(socketChannel!=null){
                // 使用 分离的 reactor 进行 后续 处理
                subReactor.register(socketChannel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " MainSubAcceptor run end");
    }
}
