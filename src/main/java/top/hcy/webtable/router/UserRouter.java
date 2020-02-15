package top.hcy.webtable.router;

import lombok.Data;

import java.util.HashMap;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.service
 * @ClassName: UserRouter
 * @Author: hcy
 * @Description:
 * @Date: 20-2-2 21:09
 * @Version: 1.0
 **/
@Data
public class UserRouter {
    private String path;
    private String name;
    private String component;
    private HashMap<String,String> meta;

    public UserRouter(String path, String name, String component,String title,String icon) {
        this.path = path;
        this.name = name;
        this.component = component;
        meta = new HashMap<>();
        meta.put("title",title);
        meta.put("icon",icon);
    }
}