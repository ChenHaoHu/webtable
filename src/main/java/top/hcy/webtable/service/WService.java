package top.hcy.webtable.service;

import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.filter.AbstractFilterChain;
import top.hcy.webtable.filter.WHandleFilter;
import top.hcy.webtable.wsql.kv.WKVType;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;


public class WService {

    public void verifyParams(WebTableContext ctx){

    };
    public void doService(WebTableContext ctx){

    };

    public JSONObject getFieldConfig(String table, String username, String field) {
        return (JSONObject) kvDBUtils.getValue( WConstants.PREFIX_FIELD+table+"."+ field, WKVType.T_MAP);
    }

    public JSONObject getTableConfig(String table, String username) {
        return (JSONObject) kvDBUtils.getValue(username+"."+ WConstants.PREFIX_TABLE + table, WKVType.T_MAP);
    }

}
