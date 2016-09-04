package edu.tongji.reserve;

import edu.tongji.common.CommonUtils;
import edu.tongji.common.Configs;

import java.io.*;
import java.net.Socket;

/**
 * Created by mi.li on 16/9/4.
 */
public class ReverseHandler implements Runnable {

    private Socket socket;

    int[] data = new int[Configs.UPLOAD_DATA_COUNT];

    ReverseHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader bd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = bd.readLine();
            while (null != line) {
                if (line.startsWith("GET /reverse?data=")) {
                    String dataStr = line.substring(line.lastIndexOf("GET /reverse?data=") + 18, line.indexOf("HTTP") - 1);
                    String[] dataStrs = dataStr.split(",");
                    for (int i = 0; i < dataStrs.length; i++) {
                        data[i] = Integer.valueOf(dataStrs[i]);
                    }
                    if (CommonUtils.checkCRC(data)) {
                        Integer id = data[4] * 256 + data[5];
                        String[] addrs = Configs.idMapToIpPort.get(id).split(":");
                        Socket s = new Socket(addrs[0],Integer.valueOf(addrs[1]));
                        OutputStream os = s.getOutputStream();
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                        bw.write(dataStr);
                        bw.flush();
                        s.close();
                    }
                }
                line = bd.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
