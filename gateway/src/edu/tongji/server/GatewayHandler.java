package edu.tongji.server;

import com.alibaba.fastjson.JSON;

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
    private String port;

    private Map<Integer, String> idMapToIpPort = new HashMap<>();

    int count = 0;

    int[] data = new int[Constants.UPLOAD_DATA_COUNT];

    GatewayHandler(Socket socket) {
        this.socket = socket;
        this.ip = socket.getInetAddress().getHostAddress();
        this.port = Integer.toString(socket.getPort());
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
                    if (checkCRC(data)) {
                        buildIDMapToIpPort(data);
                        uploadData(data);
                    }
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
        Map dataPack = new HashMap<>();
        dataPack.put("gatewayID", data[2] * 256 + data[3]);
        dataPack.put("action", data[4]);
        switch (data[4]) {
            //网关上线
            case 0X30:
            //网关下线
            case 0X31:
                break;
            //设备上线
            case 0X33:
            //设备下线
            case 0X34:
            //设备事件
            case 0X55:
                dataPack.put("deviceID", data[5] * 256 + data[6]);
                dataPack.put("deviceControlStatus", data[7]);
                dataPack.put("MCOpen", data[8]);
        }
        String json = JSON.toJSONString(dataPack);

    }

    private void buildIDMapToIpPort(int[] data) {
        Integer id = data[2] * 256 + data[3];
        if (!idMapToIpPort.containsKey(id)) {
            idMapToIpPort.put(id, ip + ":" + port);
        }
    }

    private boolean checkCRC(int[] data) {
        //TODO


        return true;
    }
}


