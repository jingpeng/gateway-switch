package edu.tongji.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by mi.li on 16/9/4.
 */
public class CommonUtils {

	public static void loadConfigs() {
		Properties pro = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream("./settings.properties");
			pro.load(in);
			String switch_port = pro.getProperty("SWITCH_PORT");
			if (null != switch_port) {
				Configs.SWITCH_PORT = Integer.valueOf(switch_port);
			}
			String switch_ip = pro.getProperty("SWITCH_IP");
			if (null != switch_ip) {
				Configs.SWITCH_IP = switch_ip;
			}
			String reverse_port = pro.getProperty("REVERSE_PORT");
			if (null != reverse_port) {
				Configs.REVERSE_PORT = Integer.valueOf(reverse_port);
			}
			String reverse_ip = pro.getProperty("REVERSE_IP");
			if (null != reverse_ip) {
				Configs.REVERSE_IP = reverse_ip;
			}
			String pool_size = pro.getProperty("POOL_SIZE");
			if (null != pool_size) {
				Configs.POOL_SIZE = Integer.valueOf(pool_size);
			}
			String upload_url = pro.getProperty("UPLOAD_URL");
			if (null != upload_url) {
				Configs.UPLOAD_URL = upload_url;
			}
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("配置文件读取失败，采用默认配置");
		} catch (IOException e) {
			System.out.println("io错误");
		}
	}

	public static byte[] intToByte(int number) {
		byte[] abyte = new byte[4];
		// "&" 与（AND），对两个整型操作数中对应位执行布尔代数，两个位都为1时输出1，否则0。
		abyte[0] = (byte) (0xff & number);
		// ">>"右移位，若为正数则高位补0，若为负数则高位补1
		abyte[1] = (byte) ((0xff00 & number) >> 8);
		abyte[2] = (byte) ((0xff0000 & number) >> 16);
		abyte[3] = (byte) ((0xff000000 & number) >> 24);
		return abyte;
	}

	public static boolean checkUploadCRC(int[] data) {
		return CRCTool.calcCrc16(data) == data[9] * 16 + data[10];
	}
	
	public static int[] fillReverseCRC(int[] data) {
		int crc = CRCTool.calcCrc16(data, 0, 7);
		data[7] = (byte)((crc >> 8) & 0xff);
		data[8] = (byte)(crc%0x100);
		return data;
	}
}
