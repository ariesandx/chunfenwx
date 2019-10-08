package com.chunfen.tomcat.nio.reactor;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * acceptor 使用线程池 处理 读时间
 */
public class PoolAcceptor implements Runnable {

    private Reactor reactor;

    private ThreadPoolExecutor pool;

    static final int PROCESSING = 3;

    public PoolAcceptor(Reactor reactor){
        this.reactor=reactor;
        pool = new ThreadPoolExecutor(3, 5, 1000, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel=reactor.getServerSocketChannel().accept();
            System.out.println(Thread.currentThread().getName() + " Acceptor accept a client");
            //调用Handler来处理channel
            if(socketChannel!=null){
                if(PROCESSING == 3) {
                    pool.execute(new ReadHandler(reactor.getSelector(), socketChannel));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
