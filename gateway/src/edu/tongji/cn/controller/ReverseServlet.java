package edu.tongji.cn.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.tongji.cn.common.CommonUtils;
import edu.tongji.cn.common.Configs;
import edu.tongji.cn.gateway.GatewayServer;

@SuppressWarnings("serial")
@WebServlet("/reverse")
public class ReverseServlet extends HttpServlet {

	int[] data = new int[Configs.REVERSE_DATA_COUNT];

	public ReverseServlet() {
		CommonUtils.loadConfigs();

		final GatewayServer server = new GatewayServer();
		Thread serverThread = new Thread() {
			public void run() {
				server.service();
			}
		};
		serverThread.start();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String lockAreaArray = request.getParameter("lockArea");
		String lockNumArray = request.getParameter("lockNum");
		String eventTypeArray = request.getParameter("eventType");

		Map<String, String[]> map = new HashMap<>();
		map.put("lockArea", lockAreaArray.split(","));
		map.put("lockNum", lockNumArray.split(","));
		map.put("eventType", new String[] { eventTypeArray });

		for (int i = 0; i < map.get("lockArea").length; i++) {
			generateBytes(map, i);
			// Ìî³äcrc
			CommonUtils.fillReverseCRC(data);
			try {
				upload(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		PrintWriter out = response.getWriter();
		out.println("success!");
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
			System.out.println("Éè±¸Î´×¢²á£¬socketÎª¿Õ");
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
