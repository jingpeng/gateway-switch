package edu.tongji.server;

import com.alibaba.fastjson.JSON;
import edu.tongji.common.CommonUtils;
import edu.tongji.common.Configs;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mi.li on 16/7/22.
 */
public class GatewayHandler implements Runnable {
    private Socket socket;
    private String ip;
    private String port;

    int count = 0;

    int[] data = new int[Configs.UPLOAD_DATA_COUNT];

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

                //检查是否为数据开头
                if (value == 0x7F) {
                    value = socket.getInputStream().read();
                    if (value == 0x7F) {
                        //数据开始
                        data[0] = 0x7F;
                        data[1] = 0x7F;
                        count = 2;
                        continue;
                    } else {
                        //如果不是连续两个FF，则不是数据开头，需要按常规处理前一个0xFF
                        handleValue(0x7F);
                    }
                }

                //检测是否为数据结尾
                if (value == 0x0D) {
                    value = socket.getInputStream().read();
                    if (value == 0x0A) {
                        if (CommonUtils.checkCRC(data)) {
                            buildIDMapToIpPort(data);
                            String json = generateJson(data);
                            System.out.println(json);
                            uploadJson(json);
                        }
                        //无论校验过不过都需重置data
                        count = 0;
                    } else {
                        //如果不是0x0D + 0x0A, 则不是数据结尾，需要按常规处理前一个0x0D
                        handleValue(0x0D);
                    }
                }
                handleValue(value);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleValue(int value) {
        if (value != -1) {
            data[count] = value;
            count++;
        }
    }

    private String generateJson(int[] data) {
        for (int i = 0; i != data.length; i++) {
            System.out.print(data[i]);
        }
        System.out.println();
        Map dataPack = new HashMap<>();
        dataPack.put("ip", ip);
        dataPack.put("port", port);
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
        return JSON.toJSONString(dataPack);
    }

    private void uploadJson(String json) {
        try {
            URL url = new URL(Configs.SERVER_IP + ":" + Configs.SERVER_PORT);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/json");
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "utf-8");
            osw.write(json);
            osw.flush();
            osw.close();
        } catch(Exception e){
            System.out.println("http请求发送失败");
        }
    }

    private void buildIDMapToIpPort(int[] data) {
        Integer id = data[2] * 256 + data[3];
        if (!Configs.idMapToIpPort.containsKey(id)) {
            Configs.idMapToIpPort.put(id, ip + ":" + port);
        }
    }
}


