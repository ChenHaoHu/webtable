package top.hcy.webtable.tools;

import java.util.HashMap;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.tools
 * @ClassName: CommonUtils
 * @Author: hcy
 * @Description:
 * @Date: 20-1-23 23:49
 * @Version: 1.0
 **/
public class CommonUtils {

    //返回短链 因为不是分布式项目 并发数小
    //根据时间戳获得36进制字符串
    public static String  getShortStr(){
        long l = System.currentTimeMillis();
        String s = Long.toString(l, 36);
        return s;
    }

    //生成锻炼分享的账户
    public static HashMap<String,String> generateShareAccount(){
        HashMap<String, String> data = new HashMap<>();
        data.put("username",getShortStr());
        data.put("passwd",getShortStr());
        return data;
    }
}