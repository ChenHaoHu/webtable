package top.hcy.webtable.service;

import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.constant.WGlobal;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.db.kv.WKVType;
import top.hcy.webtable.tools.JwtTokenUtils;

import java.util.HashMap;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.service
 * @ClassName: LoginService
 * @Author: hcy
 * @Description: 登录处理
 * @Date: 20-1-25 22:07
 * @Version: 1.0
 **/
public class UserInfoService implements WService {
    @Override
    public void verifyParams(WebTableContext ctx) {

    }

    @Override
    public void doService(WebTableContext ctx) {
        HashMap<String,String> map = new HashMap<>();
        String routers = "[\n" +
                "        {\n" +
                "          \"path\": \"/Data1\",\n" +
                "          \"children\": [\n" +
                "            {\n" +
                "              \"path\": \"index\",\n" +
                "              \"name\": \"Data1\",\n" +
                "              \"component\": \"/table/index\",\n" +
                "              \"meta\": { \"title\": \"数据表一\", \"icon\": \"table\" }\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"path\": \"/Data2\",\n" +
                "          \"children\": [\n" +
                "            {\n" +
                "              \"path\": \"index\",\n" +
                "              \"name\": \"Data2\",\n" +
                "              \"component\": \"/table/index\",\n" +
                "            \"meta\": { \"title\": \"数据表二\", \"icon\": \"table\" }\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"path\": \"/wadmin\",\n" +
                "          \"redirect\": \"/example/table\",\n" +
                "          \"name\": \"Example\",\n" +
                "          \"meta\": { \"title\": \"wadmin\", \"icon\": \"example\" },\n" +
                "          \"children\": [\n" +
                "            {\n" +
                "              \"path\": \"permission\",\n" +
                "              \"name\": \"permission\",\n" +
                "              \"component\": \"/wadmin/permission/index\",\n" +
                "              \"meta\": { \"title\": \"权限管理\", \"icon\": \"form\" }\n" +
                "            },\n" +
                "            {\n" +
                "              \"path\": \"member\",\n" +
                "              \"name\": \"member\",\n" +
                "              \"component\": \"/wadmin/member/index\",\n" +
                "              \"meta\": { \"title\": \"成员管理\", \"icon\": \"tree\" }\n" +
                "            },\n" +
                "            {\n" +
                "              \"path\": \"share\",\n" +
                "              \"name\": \"share\",\n" +
                "              \"component\": \"/wadmin/share/index\",\n" +
                "              \"meta\": { \"title\": \"分享管理\", \"icon\": \"nested\" }\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        { \"path\": \"*\", \"redirect\": \"/404\", \"hidden\": true }\n" +
                "      ]";
        map.put("name",ctx.getUsername());
        map.put("avatar",ctx.getToken());
        map.put("routers",routers);

        ctx.setRespsonseEntity(map);
    }
}