package edu.tongji.common;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mi.li on 16/7/23.
 */
public class Configs {

    //转换器端口号
    public static int SWITCH_PORT = 6600;

    //转换器IP
    public static String SWITCH_IP = "172.16.20.12";

    //反向控制端口号
    public static int REVERSE_PORT = 8080;

    //反向控制IP
    public static String REVERSE_IP = "127.0.0.1";

    //单个CPU线程池大小
    public static int POOL_SIZE = 10;

    //数据长度
    public static int UPLOAD_DATA_COUNT = 13;

    //地锁ID socket map
    public static Map<Integer, Socket> idSocketMap = new HashMap<>();

    //上报服务器地址
    public static String UPLOAD_URL = "tingche.tongji.edu.cn/httpservices/client/locked/event/new";
}
