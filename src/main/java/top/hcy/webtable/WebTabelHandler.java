package top.hcy.webtable;

import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.constant.WGlobal;
import top.hcy.webtable.common.enums.WHandlerTypeCode;
import top.hcy.webtable.common.response.WResponseEntity;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.db.kv.KVType;
import top.hcy.webtable.db.kv.KvDbUtils;
import top.hcy.webtable.filter.RequestMethodFilter;
import top.hcy.webtable.filter.RequestUrlFilter;
import top.hcy.webtable.filter.TokenJwtFilter;
import top.hcy.webtable.filter.WFiterChainImpl;
import top.hcy.webtable.tools.ParamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;


/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable
 * @ClassName: WebTabelHandler
 * @Author: hcy
 * @Description: web table 处理入口
 * @Date: 2020/1/14 19:52
 * @Version: 1.0
 */
@Slf4j
public class WebTabelHandler {

    WFiterChainImpl hPreRequest = null;



    public WebTabelHandler() {
        initFilters();
        initDefaultAccount();
    }

    private void initDefaultAccount() {
        ArrayList<HashMap> list = new ArrayList<>();
        HashMap<String,String> data = null;
        String[][] defaultAccounts = WConstants.DefaultAccounts;
        for (int i = 0; i < defaultAccounts.length; i++) {
           data = new HashMap<>();
           data.put("username",defaultAccounts[i][0]);
           data.put("passwd",defaultAccounts[i][1]);
           list.add(data);
        }
        kvDBUtils.setValue("account",list, KVType.T_LIST);
        JSONArray account = (JSONArray)kvDBUtils.getValue("account", KVType.T_LIST);
        System.out.println(account);


    }

    private void initFilters() {
        RequestMethodFilter requestMethodFilter = new RequestMethodFilter();
        RequestUrlFilter requestUrlFilter = new RequestUrlFilter();
        TokenJwtFilter tokenJwtFilter = new TokenJwtFilter();
        hPreRequest = new WFiterChainImpl(WHandlerTypeCode.HPreRequest);
        hPreRequest.addFileterOnLast(requestMethodFilter);
        hPreRequest.addFileterOnLast(requestUrlFilter);
        hPreRequest.addFileterOnLast(tokenJwtFilter);
    }

    //处理入口
    public WResponseEntity handler(HttpServletRequest request, HttpServletResponse response){
        WebTableContext ctx = new WebTableContext(request,response);
        //检查url 和 请求方法
        hPreRequest.doFilter(ctx);
        if (ctx.isError()){
          return defulteWResponseEntity(ctx);
        }
        return new WResponseEntity(ctx.getWRespCode(),ctx.getRespsonseEntity());
    }

    public WResponseEntity defulteWResponseEntity(WebTableContext ctx){
        return new WResponseEntity(ctx.getWRespCode(),ctx.getRespsonseEntity());
    }
}
