package edu.tongji;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GatewayServer {

	private static final int SERVER_PORT = 6600;

	public void receive() {
		try {
			ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
			Socket client = serverSocket.accept();
			while (true) {
				int data = client.getInputStream().read();
				if (data != -1) {
					System.out.println(data);
				} else {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		GatewayServer server = new GatewayServer();
		server.receive();
	}
}
