package com.chunfen.tomcat.nio;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketServer.class);

    // java bio socket 服务端
    @Test
    public void serverocketTest() throws Exception{
        ServerSocket serverSocket = new ServerSocket(8888);

        // 等待socket 请求
        Socket socket = serverSocket.accept();

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        LOGGER.info("server 服务端收到客服连接请求：{}", dis.readUTF());

        dos.writeUTF("server 接收连接成功");
        socket.close();
        serverSocket.close();

    }

    // java bio socket 客户端
    @Test
    public void clientSocketTest() throws Exception{

        Socket socket = new Socket("localhost", 8888);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        //发送请求
        dos.writeUTF("client 我是客户端，请求连接");

        LOGGER.info("client {}", dis.readUTF());

        socket.close();
    }
}
