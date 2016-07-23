package edu.tongji;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by mi.li on 16/7/22.
 */
public class GatewayHandler implements Runnable {
    private Socket socket;

    int count = 0;

    int[] data = new int[Constants.UPLOAD_DATA_COUNT];

    GatewayHandler(Socket socket) {
        this.socket = socket;
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
}



