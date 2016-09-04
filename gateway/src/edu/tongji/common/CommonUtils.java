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
            in = new FileInputStream("gateway/settings.properties");
            pro.load(in);
            String switch_port = pro.getProperty("SWITCH_PORT");
            if (null != switch_port) {
                Configs.SWITCH_PORT = Integer.valueOf(switch_port);
            }
            String reverse_port = pro.getProperty("REVERSE_PORT");
            if(null != reverse_port){
                Configs.REVERSE_PORT = Integer.valueOf(reverse_port);
            }
            String server_ip = pro.getProperty("SERVER_IP");
            if (null != server_ip) {
                Configs.SERVER_IP = server_ip;
            }
            String server_port = pro.getProperty("SERVER_PORT");
            if(null != server_port){
                Configs.SERVER_PORT = Integer.valueOf(server_port);
            }
            String pool_size = pro.getProperty("POOL_SIZE");
            if(null != pool_size){
                Configs.POOL_SIZE = Integer.valueOf(pool_size);
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("配置文件读取失败，采用默认配置");
        } catch (IOException e) {
            System.out.println("io错误");
        }
    }

    public static boolean checkCRC(int[] data) {
        //TODO: CRC校验


        return true;
    }
}
