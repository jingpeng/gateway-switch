package edu.tongji.reserve;

import edu.tongji.common.CommonUtils;
import edu.tongji.common.Configs;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mi.li on 16/9/4.
 */
public class ReverseHandler implements Runnable {

	private Socket socket;

	int[] data = new int[Configs.REVERSE_DATA_COUNT];

	ReverseHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			BufferedReader bd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line = bd.readLine();
			while (null != line) {
				if (line.startsWith("GET /reverse?")) {
					String dataStr = line.substring(line.lastIndexOf("GET /reverse?") + 13, line.indexOf("HTTP") - 1);
					Map<String, String[]> map = new HashMap<>();
					String[] dataStrs = dataStr.split("&");

					for (int i = 0; i < dataStrs.length; i++) {
						if (dataStrs[i].startsWith("lockArea=")) {
							map.put("lockArea", dataStrs[i].substring(9, dataStrs[i].length()).split(","));
						} else if (dataStrs[i].startsWith("lockNum=")) {
							map.put("lockNum", dataStrs[i].substring(8, dataStrs[i].length()).split(","));
						} else if ((dataStrs[i].startsWith("eventType="))) {
							String[] eventType = { dataStrs[i].substring(10, dataStrs[i].length()) };
							map.put("eventType", eventType);
						}
					}
					for (int i = 0; i < map.get("lockArea").length; i++) {
						generateBytes(map, i);
						// 填充crc
						CommonUtils.fillReverseCRC(data);
						upload(data);
						Thread.sleep(500);
					}

					PrintStream writer = new PrintStream(socket.getOutputStream(), true);
					writer.println("HTTP/1.0 200 OK");
					writer.println();
					writer.close();
				}
				line = bd.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void generateBytes(Map<String, String[]> map, int i) {
		data[0] = 0x7F;
		data[1] = 0x7F;
		data[2] = CommonUtils.intToByte(Integer.valueOf(map.get("lockArea")[i]))[1];
		data[3] = CommonUtils.intToByte(Integer.valueOf(map.get("lockArea")[i]))[0];
		data[4] = CommonUtils.intToByte(Integer.valueOf(map.get("lockNum")[i]))[1];
		data[5] = CommonUtils.intToByte(Integer.valueOf(map.get("lockNum")[i]))[0];
		data[6] = CommonUtils.intToByte(Integer.valueOf(map.get("eventType")[0]))[0];
		data[9] = 0x0D;
		data[10] = 0x0A;
	}

	private void upload(int[] data) throws IOException {
		Integer id = data[2] * 256 + data[3];
		Socket s = Configs.idSocketMap.get(id);
		if (null == s) {
			System.out.println("设备未注册，socket为空");
			return;
		}
		OutputStream os = s.getOutputStream();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
		for (int i = 0; i != data.length; i++) {
			bw.write(data[i]);
		}
		bw.flush();
	}
}
