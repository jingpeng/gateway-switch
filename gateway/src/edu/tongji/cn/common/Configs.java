package edu.tongji.cn.common;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mi.li on 16/7/23.
 */
public class Configs {

	// ת�����˿ں�
	public static int SWITCH_PORT = 5500;

	// ת����IP
	public static String SWITCH_IP = "202.120.163.225";

	// ������ƶ˿ں�
	public static int REVERSE_PORT = 8088;

	// �������IP
	public static String REVERSE_IP = "202.120.163.225";

	// ����CPU�̳߳ش�С
	public static int POOL_SIZE = 10;

	// ���ݳ���
	public static int UPLOAD_DATA_COUNT = 13;

	// ����������ݳ���
	public static int REVERSE_DATA_COUNT = 11;

	// ����ID socket map
	public static Map<Integer, Socket> idSocketMap = new HashMap<>();

	// �ϱ���������ַ
	public static String UPLOAD_URL = "tingche.tongji.edu.cn/httpservices/client/locked/event/new";
}
