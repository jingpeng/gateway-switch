package edu.tongji;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GatewayServer {

	private static final int SERVER_PORT = 6600;

	private static final int UPLOAD_DATA_COUNT = 13;

	public void receive() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
			Socket client = serverSocket.accept();
			int count = 0;
			int[] data = new int[UPLOAD_DATA_COUNT];
			while (true) {
				int value = client.getInputStream().read();
				if (value != -1) {
					data[count] = value;
					count++;
				}
				if (count == UPLOAD_DATA_COUNT) {
					count = 0;
					uploadData(data);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void uploadData(int[] data) {
		for (int i = 0; i != data.length; i++) {
			System.out.print(data[i]);
		}
		System.out.println();
	}

	public static void main(String[] args) {
		GatewayServer server = new GatewayServer();
		server.receive();
	}
}
