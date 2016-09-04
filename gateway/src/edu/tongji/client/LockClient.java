package edu.tongji.client;

import edu.tongji.server.Configs;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class LockClient {
	private static final String SWITCH_IP = "127.0.0.1";

	private byte[] gatewayOnlineData = { 0X7F, 0X7F, // 开始标志
			(byte) 0XFF, (byte) 0XFF, // 设备区域ID
			0X30, // 上线或下线
			0X00, 0X00, 0X00, 0X00, // 空
			0X6F, 0X6F, // CRC数据校验
			0X0D, 0X0A // 结束标志
	};

	private byte[] deviceRegisterData = { 0X7F, 0X7F, // 开始标志
			(byte) 0XFF, (byte) 0XFF, // 设备区域ID
			0X30, // 上线或下线
			0X00, 0X00, 0X00, 0X00, // 空
			0X6F, 0X6F, // CRC数据校验
			0X0D, 0X0A // 结束标志
	};

	public void connect() {
		try {
			Socket socket = new Socket(InetAddress.getByName(SWITCH_IP), Configs.SWITCH_PORT);
			OutputStream out = socket.getOutputStream();
			out.write(concatAll(gatewayOnlineData, deviceRegisterData));
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		LockClient client = new LockClient();
		client.connect();
	}

	private static byte[] concatAll(byte[] first, byte[]... rest) {
		int totalLength = first.length;
		for (byte[] array : rest) {
			totalLength += array.length;
		}
		byte[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (byte[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}
}
