package top.hcy.webtable.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.constant.WGlobal;
import top.hcy.webtable.db.kv.WKVType;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.service
 * @ClassName: GetSharePermissionService
 * @Author: hcy
 * @Description:
 * @Date: 20-2-4 22:43
 * @Version: 1.0
 **/
public class GetSharePermissionService implements WService {
    @Override
    public void verifyParams(WebTableContext ctx) {

    }

    @Override
    public void doService(WebTableContext ctx) {
        String username = ctx.getUsername();
        JSONArray tables = (JSONArray) kvDBUtils.getValue(username + ".tables", WKVType.T_LIST);

        int size = tables.size();
        JSONObject res = new JSONObject();

        for (int i = 0; i < size; i++) {
            JSONObject table = (JSONObject)kvDBUtils.getValue(username + "." + WConstants.PREFIX_TABLE + tables.get(i), WKVType.T_MAP);
            JSONObject item =new JSONObject();
            item.put("permission",table.get("permission"));
            item.put("alias",table.get("alias"));
            JSONArray fields  = (JSONArray)table.get("fields");
            JSONObject fieldsAlias = new JSONObject();
            int fieldsSize = fields.size();
            for (int j = 0; j < fieldsSize; j++) {
                JSONObject field = (JSONObject)kvDBUtils.getValue(username + "." + WConstants.PREFIX_FIELD + tables.get(i)+"."+fields.get(j), WKVType.T_MAP);
                fieldsAlias.put(fields.getString(j),field.getString("alias"));
            }
            item.put("fields",fieldsAlias);
            res.put(tables.getString(i),item);
        }
        ctx.setRespsonseEntity(res);
    }
}