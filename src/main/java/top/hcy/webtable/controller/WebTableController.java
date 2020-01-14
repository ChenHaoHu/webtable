package top.hcy.webtable.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class WebTableController {


    public WebTableController() {
      //初始化处理链

    }


    @RequestMapping("/tt")
    public String getSome(HttpServletRequest reuqest, HttpServletResponse response){




        return "_______";
    }
}
