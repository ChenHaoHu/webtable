package top.hcy.webtable.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.enums.WRespCode;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;


@Slf4j
public class ParamUtils {

    //从request获取参数

    public static void getParamsFromRequest(WebTableContext ctx, HttpServletRequest request){
        ServletInputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            String tmp = "";
            StringBuffer str = new StringBuffer();
//            int len  = 100;
//            int b;
//            byte[] bytes = new byte[len];
//            StringBuffer str = new StringBuffer();
//            while ((b = inputStream.read(bytes))!=-1){
//                str.append(new String(bytes), 0, b);
//            }
//            System.out.println(str.toString());
//            System.out.println("------------");

            while ((tmp = br.readLine())!=null){
                str.append(tmp);
            }
            JSONObject jsonObject = JSON.parseObject(str.toString());
            ctx.setParams(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.setError(true);
            ctx.setWRespCode(WRespCode.REQUEST_PARAM_ERROR);
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
