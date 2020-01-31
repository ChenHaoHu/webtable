package top.hcy.webtable.service;

import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WGlobal;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.db.kv.WKVType;

import java.util.ArrayList;
import java.util.HashMap;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.service
 * @ClassName: UpdateTableDataService
 * @Author: hcy
 * @Description: 更新数据
 * @Date: 20-1-30 23:42
 * @Version: 1.0
 **/
public class GetKvDataService implements WService{
    @Override
    public void verifyParams(WebTableContext ctx) {

    }

    @Override
    public void doService(WebTableContext ctx) {
        ArrayList<String> allKeys = kvDBUtils.getAllKeys();
        HashMap<String,Object> data = new HashMap<>();
        for (int i = 0; i < allKeys.size(); i++) {
            data.put(allKeys.get(i),
                    kvDBUtils.getValue(allKeys.get(i), WKVType.T_STRING));
        }
        ctx.setRespsonseEntity(data);
    }
}