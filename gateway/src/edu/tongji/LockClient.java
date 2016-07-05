package edu.tongji;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class LockClient {
	private static final String SERVER_IP = "127.0.0.1";

	private static final int SERVER_PORT = 6600;

	private byte[] data = { 0X7F, 0X7F, 0X6F };

	public void connect() {
		try {
			Socket socket = new Socket(InetAddress.getByName(SERVER_IP), SERVER_PORT);
			OutputStream out = socket.getOutputStream();
			out.write(data);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		LockClient client = new LockClient();
		client.connect();
	}
}
