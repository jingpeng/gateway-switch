package edu.tongji.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
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

    public static void main(String[] args) {
        loadConfigs();
        GatewayServer server = new GatewayServer();
        server.service();
    }

    private static void loadConfigs() {
        Properties pro = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream("gateway/settings.properties");
            pro.load(in);
            String switch_port = pro.getProperty("SWITCH_PORT");
            if (null != switch_port) {
                Configs.SWITCH_PORT = Integer.valueOf(switch_port);
            }
            String server_ip = pro.getProperty("SERVER_IP");
            if (null != server_ip) {
                Configs.SERVER_IP = server_ip;
            }
            String server_port = pro.getProperty("SERVER_PORT");
            if(null != server_port){
                Configs.SERVER_PORT = Integer.valueOf(server_port);
            }
            String pool_size = pro.getProperty("POOL_SIZE");
            if(null != pool_size){
                Configs.POOL_SIZE = Integer.valueOf(pool_size);
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("配置文件读取失败，采用默认配置");
        } catch (IOException e) {
            System.out.println("io错误");
        }
    }
}
