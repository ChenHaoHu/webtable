package top.hcy.webtable.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hcy.webtable.WebTabelHandler;
import top.hcy.webtable.common.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class WebTableController {

    WebTabelHandler webTabelHandler;
    public WebTableController() {
      //初始化处理链
         webTabelHandler = new WebTabelHandler();
    }


    @RequestMapping("/webtable")
    public ResponseEntity getSome(HttpServletRequest reuqest, HttpServletResponse response){
        ResponseEntity handler = webTabelHandler.handler(reuqest, response);
        return handler;
    }
}
