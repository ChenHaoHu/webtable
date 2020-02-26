package top.hcy.webtable.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hcy.webtable.WebTableBootStrap;
import top.hcy.webtable.common.response.WResponseEntity;
import top.hcy.webtable.db.kv.WKVType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;

@RestController
public class WebTableController {

    WebTableBootStrap webTabelHandler;
    public WebTableController() {
      //初始化处理链
         webTabelHandler = new WebTableBootStrap();
    }

    @RequestMapping("/webtable")
    public WResponseEntity getSome(HttpServletRequest reuqest, HttpServletResponse response){
        WResponseEntity handler = webTabelHandler.handler(reuqest, response);
        return handler;
    }


    @RequestMapping("/tmp/json")
    public  Object json(){
        ArrayList<String> allKeys = kvDBUtils.getAllKeys();
        HashMap<String,Object> data = new HashMap<>();
        for (int i = 0; i < allKeys.size(); i++) {
            data.put(allKeys.get(i), (String) kvDBUtils.getValue(allKeys.get(i), WKVType.T_STRING));

        };
      return data;
    }
}
