package edu.tongji;

import edu.tongji.common.CommonUtils;
import edu.tongji.reserve.ReverseServer;
import edu.tongji.server.GatewayServer;

/**
 * Created by mi.li on 16/9/4.
 */
public class Main {
    public static void main(String[] args) {
        CommonUtils.loadConfigs();
        GatewayServer server = new GatewayServer();
        Thread serverThread = new Thread(){
            public void run(){
                server.service();
            }
        };
        serverThread.start();

        ReverseServer reverseServer = new ReverseServer();
        Thread reverseServerThread = new Thread(){
            public void run(){
                reverseServer.service();
            }
        };
        reverseServerThread.start();
    }
}
