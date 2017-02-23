package edu.tongji.cn.common;

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
			System.out.println("�����ļ���ȡʧ�ܣ�����Ĭ������");
		} catch (IOException e) {
			System.out.println("io����");
		}
	}

	public static byte[] intToByte(int number) {
		byte[] abyte = new byte[4];
		// "&" �루AND�������������Ͳ������ж�Ӧλִ�в�������������λ��Ϊ1ʱ���1������0��
		abyte[0] = (byte) (0xff & number);
		// ">>"����λ����Ϊ�������λ��0����Ϊ�������λ��1
		abyte[1] = (byte) ((0xff00 & number) >> 8);
		abyte[2] = (byte) ((0xff0000 & number) >> 16);
		abyte[3] = (byte) ((0xff000000 & number) >> 24);
		return abyte;
	}

	public static boolean checkUploadCRC(int[] data) {
		int crc = CRCTool.calcCrc16(data, 0, 9);
		int crcH = (crc >> 8) & 0xff;
		int crcL = (crc % 0x100) & 0xff;
		return (crcH == data[9]) && (crcL == data[10]);
	}

	public static int[] fillReverseCRC(int[] data) {
		int crc = CRCTool.calcCrc16(data, 0, 7);
		data[7] = (crc >> 8) & 0xff;
		data[8] = (crc % 0x100) & 0xff;
		return data;
	}
}
