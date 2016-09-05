package edu.tongji.server;

import edu.tongji.common.Configs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GatewayServer {

    ServerSocket serverSocket = null;

    private ExecutorService executorService;//线程池

    public GatewayServer() {
        try {
            serverSocket = new ServerSocket(Configs.SWITCH_PORT);
            executorService = Executors.newFixedThreadPool(Configs.POOL_SIZE);
            System.out.println("转换器启动");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void service() {
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                executorService.execute(new GatewayHandler(socket));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
