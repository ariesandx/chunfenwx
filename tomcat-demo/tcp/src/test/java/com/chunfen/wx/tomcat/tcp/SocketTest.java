package com.chunfen.wx.tomcat.tcp;

import org.junit.Test;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SocketTest {

    class RequestHandler extends Thread{

        private Socket socket;


        public RequestHandler(Socket socket) {
            super();
            this.socket = socket;
        }

        @Override
        public void run() {
            try (PrintWriter printWriter = new PrintWriter(socket.getOutputStream())) {
                printWriter.println("hello world！");
                printWriter.println("this is server ");
                printWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /*
    bio 基本演示
     */
    @Test
    public void socket1() throws Exception{
        Thread sThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(8888);
                    // 服务器 等待连接
                    while (true) {
                        Socket socket = serverSocket.accept();
                        new RequestHandler(socket).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        // 服务器启动
        sThread.start();

        // Socket 客户端（接收信息并打印）
        try (Socket cSocket = new Socket(InetAddress.getLocalHost(), 8888)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
            bufferedReader.lines().forEach(s -> System.out.println("客户端：" + s));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    bio 基本演示 + 连接池
     */
    @Test
    public void socket2() throws Exception{

        Executor executor = Executors.newFixedThreadPool(8);

        Thread sThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(8888);
                    // 服务器 等待连接
                    while (true) {
                        Socket socket = serverSocket.accept();
                        executor.execute(new RequestHandler(socket));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        sThread.start();

        // Socket 客户端（接收信息并打印）
        try (Socket cSocket = new Socket(InetAddress.getLocalHost(), 8888)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
            bufferedReader.lines().forEach(s -> System.out.println("客户端：" + s));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    nio 基本演示
     */
    @Test
    public void socket3() throws Exception{

        Thread sThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (Selector selector = Selector.open();
                     ServerSocketChannel serverSocket = ServerSocketChannel.open();) {// 创建 Selector 和 Channel
                    serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 8888));
                    serverSocket.configureBlocking(false);
                    // 注册到 Selector，并说明关注点
                    serverSocket.register(selector, SelectionKey.OP_ACCEPT);
                    while (true) {
                        selector.select();// 阻塞等待就绪的 Channel，这是关键点之一
                        Set<SelectionKey> selectedKeys = selector.selectedKeys();
                        Iterator<SelectionKey> iter = selectedKeys.iterator();
                        while (iter.hasNext()) {
                            SelectionKey key = iter.next();
                            // 生产系统中一般会额外进行就绪状态检查
                            sayHelloWorld((ServerSocketChannel) key.channel());
                            iter.remove();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void sayHelloWorld(ServerSocketChannel server) throws IOException {
                try (SocketChannel client = server.accept();) {
                    client.write(Charset.defaultCharset().encode("Hello world!"));
                }
            }

        });
        sThread.start();

        Thread.sleep(200);

        // Socket 客户端（接收信息并打印）
        try (SocketChannel cSocket = SocketChannel.open()) {

            SocketAddress socketAddress = new InetSocketAddress(InetAddress.getLocalHost(),8888);

            cSocket.connect(socketAddress);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int read=0;
            while((read = cSocket.read(buffer)) != -1){
                buffer.flip();
                byte[] bytes = new byte[read];
                buffer.get(bytes);
                baos.write(bytes);
                buffer.clear();
            }
            String s1 = new String(baos.toByteArray(), "utf-8");
            System.out.println("client :" + s1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
