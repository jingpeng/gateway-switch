package edu.tongji.server;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mi.li on 16/7/22.
 */
public class GatewayHandler implements Runnable {
    private Socket socket;
    private String ip;

    private Map<String, String> IDIPMap = new HashMap<>();

    int count = 0;

    int[] data = new int[Constants.UPLOAD_DATA_COUNT];

    GatewayHandler(Socket socket) {
        this.socket = socket;
        this.ip = socket.getInetAddress().getHostAddress();
    }

    @Override
    public void run() {
        while (true) {
            int value = 0;
            try {
                value = socket.getInputStream().read();

                if (value != -1) {
                    data[count] = value;
                    count++;
                }
                if (count == Constants.UPLOAD_DATA_COUNT) {
                    count = 0;
                    buildIDIPMap(data);
                    uploadData(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadData(int[] data) {

        for (int i = 0; i != data.length; i++) {
            System.out.print(data[i]);
        }
        System.out.println();
    }

    private void buildIDIPMap(int[] data) {
        StringBuilder id = new StringBuilder();
        for (int i = 2; i <= 3; i++) {
            id.append(data[i]);
        }
        if (!IDIPMap.containsKey(id)) {
            IDIPMap.put(id.toString(),ip);
        }
    }
}



