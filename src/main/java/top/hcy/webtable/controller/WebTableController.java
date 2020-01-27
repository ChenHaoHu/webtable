package top.hcy.webtable.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hcy.webtable.BootStrap;
import top.hcy.webtable.common.response.WResponseEntity;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class WebTableController {

    BootStrap webTabelHandler;
    public WebTableController() {
      //初始化处理链
         webTabelHandler = new BootStrap();
    }

    @RequestMapping("/webtable")
    public WResponseEntity getSome(HttpServletRequest reuqest, HttpServletResponse response){
        WResponseEntity handler = webTabelHandler.handler(reuqest, response);
        return handler;
    }

}
