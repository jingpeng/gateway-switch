package edu.tongji.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mi.li on 16/7/23.
 */
public class Configs {

    //转换器端口号
    public static int SWITCH_PORT = 6600;

    //反向控制端口号
    public static int REVERSE_PORT = 8080;

    //服务器ip
    public static String SERVER_IP = "10.60.30.4";

    //服务器端口号
    public static int SERVER_PORT = 8080;

    //单个CPU线程池大小
    public static int POOL_SIZE = 10;

    //数据长度
    public static int UPLOAD_DATA_COUNT = 13;

    //地锁IP ID map
    public static Map<Integer, String> idMapToIpPort = new HashMap<>();

    //上报服务器地址
    public static String UPLOAD_URL = "tingche.tongji.edu.cn/httpservices/client/locked/event/new";
}
