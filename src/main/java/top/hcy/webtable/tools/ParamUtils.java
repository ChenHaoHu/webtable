package top.hcy.webtable.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.common.WebTableContext;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.tools
 * @ClassName: ParamUtil
 * @Author: hcy
 * @Description: 参数处理工具
 * @Date: 2020/1/15 10:30
 * @Version: 1.0
 */
@Slf4j
public class ParamUtils {

    //从request获取参数

    public static void getParamsFromRequest(WebTableContext ctx, HttpServletRequest request){
        ServletInputStream inputStream = null;
        try {
             inputStream = request.getInputStream();
            int len  = 100;
            int b;
            byte[] bytes = new byte[len];
            StringBuffer str = new StringBuffer();
            while ((b = inputStream.read(bytes))!=-1){
                str.append(new String(bytes), 0, b);
            }
            JSONObject jsonObject = JSON.parseObject(str.toString());
            ctx.setParams(jsonObject);
        } catch (IOException e) {
       //     e.printStackTrace();
            ctx.setError(true);
            if(log.isErrorEnabled()){
                log.error("request params parse error");
            }
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ;
    }
}
