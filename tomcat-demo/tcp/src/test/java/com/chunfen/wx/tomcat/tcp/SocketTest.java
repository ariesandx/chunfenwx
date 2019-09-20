package com.chunfen.wx.tomcat.tcp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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
    nio socket 基本演示
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
                            sendData((ServerSocketChannel) key.channel());
                            iter.remove();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void sendData(ServerSocketChannel server) throws IOException {
                try (SocketChannel sSocket = server.accept();) {

                    Message<String> stringMessage = new Message<>();
                    stringMessage.setCode(1);
                    stringMessage.setData("Hello World!");
                    stringMessage.setMessage("你好");

                    System.out.println("server:");
                    System.out.println("server send: "+ new ObjectMapper().writeValueAsString(stringMessage));
                    sSocket.write(ByteBuffer.wrap(toBytes(stringMessage)));
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
            System.out.println("client receive: " + new ObjectMapper().writeValueAsString(objectMessage));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <T> Message<T> receiveData(SocketChannel channel){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        int read=0;
        try {
            while((read = channel.read(buffer)) != -1){
                buffer.flip();
                byte[] bytes = new byte[read];
                buffer.get(bytes);
                baos.write(bytes);
                buffer.clear();
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
            return new ObjectMapper().readValue(bytes, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public byte[] toBytes(Object object){
        try {
            return new ObjectMapper().writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}

class Message<T>{
    private Integer code;
    private T data;
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
