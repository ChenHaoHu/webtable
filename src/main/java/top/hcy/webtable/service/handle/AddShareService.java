package top.hcy.webtable.service.handle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.annotation.common.WHandleService;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.enums.WHandlerType;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.db.kv.WKVType;
import top.hcy.webtable.service.WService;
import top.hcy.webtable.tools.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.service
 * @ClassName: AddShareService
 * @Author: hcy
 * @Description:
 * @Date: 20-2-5 0:49
 * @Version: 1.0
 **/
@WHandleService(WHandlerType.ASHAREDATA)
public class AddShareService implements WService {
    @Override
    public void verifyParams(WebTableContext ctx) {
        //验证管理员身份

    }

    @Override
    public void doService(WebTableContext ctx) {

        JSONArray shareslist = (JSONArray) kvDBUtils.getValue("shareslist", WKVType.T_LIST);

        if (shareslist == null){
            shareslist = new JSONArray();
        }

        JSONObject data = CommonUtils.generateShareAccount();
        JSONObject params = ctx.getParams();
        String marks = params.getString("marks");
        JSONArray permissions = params.getJSONArray("permission");
        data.put("marks",marks);
        data.put("status","1");
        data.put("permission",permissions);
        shareslist.add(data);

        boolean b = kvDBUtils.setValue("shareslist", shareslist, WKVType.T_LIST);

        if(b == false){
            ctx.setWRespCode(WRespCode.INSERT_FAIL);
            return;
        }

        int size = permissions.size();
        ArrayList<String> tableList = new ArrayList<>();
        HashMap<String, ArrayList<String>> permissionsMap = new HashMap<>();
        HashMap<String, ArrayList<String>> fieldsMap = new HashMap<>();
        HashMap<String, ArrayList<String>> abstractfieldsMap = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String permission = permissions.getString(i);
            if(permission.startsWith("permission.")){
                String[] split = permission.split("\\.");
                if (split.length == 3){
                    ArrayList<String> list = permissionsMap.get(split[1]);
                    if (list == null){
                        list = new ArrayList<>();
                    }
                    list.add(split[2]);
                    permissionsMap.put(split[1],list);
                }
            }
            if(permission.startsWith("fields.")){
                String[] split = permission.split("\\.");
                if (split.length == 3){
                    ArrayList<String> list = fieldsMap.get(split[1]);
                    if (list == null){
                        list = new ArrayList<>();
                    }
                    list.add(split[2]);
                    fieldsMap.put(split[1],list);
                }
                if (split.length == 4){
                    ArrayList<String> list = abstractfieldsMap.get(split[1]);
                    if (list == null){
                        list = new ArrayList<>();
                    }
                    list.add(split[3]);
                    abstractfieldsMap.put(split[1],list);
                }
            }
        }

        for (String table :  permissionsMap.keySet()) {
            tableList.add(table);
            JSONObject value = (JSONObject)kvDBUtils.getValue(WConstants.PREFIX_TABLE + table, WKVType.T_MAP);
            value.put("permission",permissionsMap.get(table));
            value.put("fields",fieldsMap.get(table));
            value.put("abstractfields",abstractfieldsMap.get(table));
            b = kvDBUtils.setValue(data.getString("username") + "." + WConstants.PREFIX_TABLE + table, value, WKVType.T_MAP);
            if(b == false){
                ctx.setWRespCode(WRespCode.INSERT_FAIL);
                return;
            }
            b = kvDBUtils.setValue(data.getString("username") + ".tables" , tableList, WKVType.T_LIST);
        }

        if(b == false){
            ctx.setWRespCode(WRespCode.INSERT_FAIL);
            return;
        }

        ctx.setRespsonseEntity(data);
        ctx.setWRespCode(WRespCode.INSERT_SUCCESS);
    }
}