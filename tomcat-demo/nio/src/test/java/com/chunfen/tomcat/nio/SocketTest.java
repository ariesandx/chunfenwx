package com.chunfen.tomcat.nio;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SocketTest extends BaseTest{

    /**
     *  bio 处理 handler
     */
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
                    // serverSocket 绑定 ip port
                    ServerSocket serverSocket = new ServerSocket(8888);
                    while (true) {
                        // serverSocket 等待连接
                        Socket socket = serverSocket.accept();
                        //获取socket 后开启线程处理
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
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(cSocket.getInputStream()));
            //读取数据并打印
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

        try (Socket cSocket = new Socket(InetAddress.getLocalHost(), 8888)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
            bufferedReader.lines().forEach(s -> System.out.println("客户端：" + s));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    nio socket 基本演示 非阻塞 同步 io
     */
    @Test
    public void socket3() throws Exception{

        Thread sThread = new Thread(new Runnable() {
            @Override
            public void run() {

                // 创建 server 端
                // 1.创建Selector
                // 2.创建 ServerSocketChannel
                // 3.绑定 ip port
                // 4.设置为 非阻塞，注册到 Selector 中
                // 5.selector.select() 等待 就绪的 channel
                // 6. 有就绪的 channel 将它取出 并 accept  拿到 socketChannel 进行 交互
                try (Selector selector = Selector.open();
                     ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();) {// 创建 Selector 和 Channel
                    serverSocketChannel.bind(new InetSocketAddress(InetAddress.getLocalHost(), 8888));
                    //阻塞模式下，注册操作是不允许的
                    serverSocketChannel.configureBlocking(false);

                    // 注册到 Selector，并说明关注点
                    // 5.把通道注册到多路复用器上，并监听阻塞事件
                    /**
                     * SelectionKey.OP_READ     : 表示关注读数据就绪事件
                     * SelectionKey.OP_WRITE     : 表示关注写数据就绪事件
                     * SelectionKey.OP_CONNECT: 表示关注socket channel的连接完成事件
                     * SelectionKey.OP_ACCEPT : 表示关注server-socket channel的accept事件
                     */
                    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                    while (true) {
                        /**
                         * a.select() 阻塞到至少有一个通道在你注册的事件上就绪
                         * b.select(long timeOut) 阻塞到至少有一个通道在你注册的事件上就绪或者超时timeOut
                         * c.selectNow() 立即返回。如果没有就绪的通道则返回0
                         * select方法的返回值表示就绪通道的个数。
                         */
                        selector.select();// 阻塞等待就绪的 Channel，这是关键点之一
                        Set<SelectionKey> selectedKeys = selector.selectedKeys();
                        Iterator<SelectionKey> iter = selectedKeys.iterator();
                        while (iter.hasNext()) {
                            SelectionKey key = iter.next();

                            // 6.只获取有效的key
                            if (!key.isValid()){
                                continue;
                            }
                            // 阻塞状态处理
                            if (key.isAcceptable()){
//                                accept(key);
                            }
                            // 可读状态处理
                            if (key.isReadable()){
//                                read(key);
                            }

                            // 生产系统中一般会额外进行就绪状态检查
                            sendData((ServerSocketChannel) key.channel());
                            iter.remove();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        sThread.start();

        Thread.sleep(200);

        //创建 client端
        //1.创建 SocketChannel
        //2.连接到 ip port
        // 3.和服务端交互 read or write

        // Socket 客户端（接收信息并打印）
        try (SocketChannel cSocket = SocketChannel.open()) {

            cSocket.connect(new InetSocketAddress(InetAddress.getLocalHost(),8888));

            Message<String> objectMessage = receiveData(cSocket);

            System.out.println("client receive:");
            System.out.println(objectMessage.getClass());
            System.out.println(objectMessage.getData().getClass());
            System.out.println("client receive: " + objectMapper.writeValueAsString(objectMessage));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    nio socket 异步 基本演示   非阻塞 异步 io
     */
    @Test
    public void socket4() throws Exception{

        Thread sThread = new Thread(new Runnable() {
            @Override
            public void run() {

                // 创建 server 端
                try (AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open()
                             .bind(new InetSocketAddress(InetAddress.getLocalHost(), 8888));) {// 创建 Selector 和 Channel


                    serverSocketChannel.accept(this, new CompletionHandler<AsynchronousSocketChannel, Object>() {
                        @Override
                        public void completed(AsynchronousSocketChannel result, Object attachment) {
                            System.out.println(Thread.currentThread().getName() + " server accept completed");
                            try {
                                sendDataAsync(result);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failed(Throwable exc, Object attachment) {
                            exc.printStackTrace();
                        }
                    });
                    System.out.println(Thread.currentThread().getName() + " server start.");

                    // 一直阻塞 不让服务器停止，真实环境是在tomcat下运行，所以不需要这行代码
                    Thread.sleep(Integer.MAX_VALUE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        sThread.start();

        Thread.sleep(200);

        //创建 client端
        //1.创建 SocketChannel
        //2.连接到 ip port
        // 3.和服务端交互 read or write

        // Socket 客户端（接收信息并打印）
        try (AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open()) {

            socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8888), socketChannel, new CompletionHandler<Void, AsynchronousSocketChannel>() {
                @Override
                public void completed(Void result, AsynchronousSocketChannel attachment) {
                    System.out.println(Thread.currentThread().getName() + " client connect completed");
                    receiveDataAsync(attachment);
                }

                @Override
                public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                    exc.printStackTrace();
                }
            });
            System.out.println(Thread.currentThread().getName() + " client start.");

            // 一定要睡， 否则  主程序退出， channel 关闭
            Thread.sleep(20 * 1000);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 异步接收
     * @param socketChannel
     * @throws IOException
     */
    private void sendDataAsync(AsynchronousSocketChannel socketChannel) throws Exception {
        Message<String> stringMessage = new Message<>();
        stringMessage.setCode(1);
        stringMessage.setData("Hello World!");
        stringMessage.setMessage("你好");

        socketChannel.write(ByteBuffer.wrap(toBytes(stringMessage)), socketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
            @Override
            public void completed(Integer result, AsynchronousSocketChannel attachment) {
                System.out.println(Thread.currentThread().getName() + " server write completed..");
            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                exc.printStackTrace();
            }
        });
        System.out.println(Thread.currentThread().getName() + " socketChannel.write");

    }


    /**
     * 同步发送
     * @param serverSocketChannel
     * @throws IOException
     */
    private void sendData(ServerSocketChannel serverSocketChannel) throws IOException {
        try (SocketChannel sSocket = serverSocketChannel.accept();) {

            Message<String> stringMessage = new Message<>();
            stringMessage.setCode(1);
            stringMessage.setData("Hello World!");
            stringMessage.setMessage("你好");

            System.out.println("server:");
            System.out.println("server send: "+ objectMapper.writeValueAsString(stringMessage));
            sSocket.write(ByteBuffer.wrap(toBytes(stringMessage)));
        }
    }

    /**
     * 异步发送
     * @param channel
     * @return
     */
    private void receiveDataAsync(AsynchronousSocketChannel channel){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        channel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {

                attachment.flip();//将 limit = pos; pos = 0 目的是 让 get 方法 从 开头读取
                byte[] bytes = new byte[result];
                attachment.get(bytes);
                try {
                    baos.write(bytes);// 读到的 bytes 写入 目标数组
                } catch (IOException e) {
                    e.printStackTrace();
                }
                attachment.clear();//将 pos = 0; limit = capacity; 准备下次 读取 新数据

                Message message = toObject(baos.toByteArray(), Message.class);
                try {
                    System.out.println(Thread.currentThread().getName() + " client read complete" + new ObjectMapper().writeValueAsString(message));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });
        System.out.println(Thread.currentThread().getName() + " channel.read");
    }

    /**
     * 同步接收
     * @param channel
     * @param <T>
     * @return
     */
    private <T> Message<T> receiveData(SocketChannel channel){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        int read=0;
        try {
            while((read = channel.read(buffer)) != -1){
                buffer.flip();//将 limit = pos; pos = 0 目的是 让 get 方法 从 开头读取
                byte[] bytes = new byte[read];
                buffer.get(bytes);
                baos.write(bytes);// 读到的 bytes 写入 目标数组
                buffer.clear();//将 pos = 0; limit = capacity; 准备下次 读取 新数据
            }
            return toObject(baos.toByteArray(), Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数组转对象
     * @param bytes
     * @return
     */
    public <T> T toObject (byte[] bytes, Class<T> clazz) {

        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public byte[] toBytes(Object object){
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
