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

    private Map<Integer, Integer> IpGateWayIDap = new HashMap<Integer, Integer>();

    public GatewayServer() {
        try {
            serverSocket = new ServerSocket(Configs.SWITCH_PORT);
            //Runtime的availableProcessor()方法返回当前系统的CPU数目.
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * Configs.POOL_SIZE);
            System.out.println("转换器启动");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void service() {
        while (true) {
            Socket socket = null;
            try {
                //接收客户连接,只要客户进行了连接,就会触发accept();从而建立连接
                socket = serverSocket.accept();
                executorService.execute(new GatewayHandler(socket));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
